package com.sparta.board.service;

import com.sparta.board.dto.PostRequestDto;
import com.sparta.board.dto.PostResponseDto;
import com.sparta.board.dto.PostWithCommentsResponseDto;
import com.sparta.board.entity.Post;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.PostRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //게시글 get
    @Transactional(readOnly = true)
    public List<PostWithCommentsResponseDto> getPosts() {

        List<Post> allPost = postRepository.findAllMemoByOrderByModifiedAtDesc();
        List<PostWithCommentsResponseDto> allPostWithCommentsResponse = new ArrayList<>();

        for (Post post : allPost){
              allPostWithCommentsResponse.add(new PostWithCommentsResponseDto(post));
        }

        return allPostWithCommentsResponse;
    }
    //게시글 post
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        //request에서 토큰 갖고오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //토큰이 있는 경우에만 게시글 추가 가능
        if(token != null){
            //토큰 검증
            if(jwtUtil.validateToken(token)){
                //토큰에서 사용자 정보 갖고오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("Token Error");
            }

            //토큰에서 갖고온 사용자 정보를 사용해서 DB조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            //요청받은 DTO로 DB에 저장할 객체 만들기
            Post post = new Post(requestDto,user); //Post class에서 만든 생성자 이용
            postRepository.save(post);
            return new PostResponseDto(post);
        } else{
            return null;
        }
    }

    //특정 게시글 조회
    @Transactional(readOnly = true)
    public PostWithCommentsResponseDto getPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return new PostWithCommentsResponseDto(post);
    }


    //특정게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request){
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 관심상품 조회 가능
        if (token != null) {

            // Token 검증
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
            //사용자 권한 갖고오기
            UserRoleEnum userRoleEnum = user.getRole();
            System.out.println("role :" + userRoleEnum);
            if(userRoleEnum == UserRoleEnum.USER){
                Post post = postRepository.findByIdAndUser(id, user).orElseThrow(
                        () -> new IllegalArgumentException("해당 사용자가 작성한 게시글이  존재하지 않습니다.")
                );
                post.update(requestDto);
                postRepository.save(post);
                return new PostResponseDto(post);
            }else{
                Post post = postRepository.findById(id).orElseThrow(
                        () ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
                post.update(requestDto);
                postRepository.save(post);
                System.out.println("관리자에 의해 수정되었습니다.");
                return new PostResponseDto(post);
            }
        }else{
            return  null;
        }
    }



    @Transactional
    public boolean deletePost(Long id, HttpServletRequest request){
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 관심상품 조회 가능
        if (token != null) {

            // Token 검증
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
            UserRoleEnum userRoleEnum = user.getRole();
            System.out.println("role :" + userRoleEnum);
            if(postRepository.existsById(id)){
                if(userRoleEnum == UserRoleEnum.ADMIN){
                    postRepository.deleteById(id);
                    System.out.println("관리자에 의해 삭제되었습니다.");
                }else{
                    if(postRepository.existsByIdAndUser(id, user)){
                        postRepository.deleteById(id);
                    }else{
                        throw  new IllegalArgumentException("사용자가 작성한 게시글이 아닙니다.");
                    }
                }
            }else{
                throw new IllegalArgumentException("해당 게시글이 없습니다.");
            }

            return  true;
        } else{
            return false;
        }

    }


}
