package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.Post;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.PostRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            //게시글 조회 및 예외 발생
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("대상 게시글이 없습니다.")
            );
            // comment 생성
            Comment comment = new Comment(requestDto, user, post);
            commentRepository.save(comment);
            return new CommentResponseDto(comment);
        } else {
            return null;
        }

    }

    @Transactional
    public CommentResponseDto updateComment(Long id, Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            //게시글 조회 및 예외 발생
            if (!postRepository.existsById(id)) {
                throw new IllegalArgumentException("해당 게시글이 없습니다.");
            }
            UserRoleEnum userRoleEnum = user.getRole();
            System.out.println("role = " + userRoleEnum);


            if (userRoleEnum == UserRoleEnum.ADMIN) {
                Comment comment = commentRepository.findById(commentId).orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글이 없습니다.")
                );
                comment.update(requestDto);
                commentRepository.save(comment);
                System.out.println("관라자에 의해 수정되었습니다.");
                return new CommentResponseDto(comment);
            } else {
                if (commentRepository.existsById(commentId)) {
                    Comment comment = commentRepository.findByIdAndUserId(commentId, user).orElseThrow(
                            () -> new IllegalArgumentException("사용자가 작성한 댓글이 아닙니다.")
                    );
                    comment.update(requestDto);
                    commentRepository.save(comment);
                    return new CommentResponseDto(comment);
                } else {
                    throw new IllegalArgumentException("해당 댓글이 없습니다.");
                }
            }
        } else {
            return null;
        }
    }

        public boolean deleteComment (Long id, Long commentId, HttpServletRequest request){
            String token = jwtUtil.resolveToken(request);
            Claims claims;
            if (token != null) {
                //Token 검증
                // Token 검증
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보 가져오기
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("Token Error");
                }

                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                if (!postRepository.existsById(id)) {
                    throw new IllegalArgumentException("해당 게시글이 없습니다.");
                }
                // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
                UserRoleEnum userRoleEnum = user.getRole();
                System.out.println("role = " + userRoleEnum);

                if (commentRepository.existsById(commentId)) {
                    if (userRoleEnum == UserRoleEnum.ADMIN) {
                        commentRepository.deleteById(commentId);
                        System.out.println("관리자에 의해 삭제되었습니다.");
                    } else {
                        if (commentRepository.existsByUser(user)) {
                            commentRepository.deleteById(commentId);
                        } else {
                            throw new IllegalArgumentException("사용자가 작성한 댓글이 아닙니다.");
                        }
                    }
                } else {
                    throw new IllegalArgumentException("해당 댓글이 없습니다.");
                }
                return true;
            } else {
                return false;
            }
        }



}
