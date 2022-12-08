package com.sparta.board.controller;


import com.sparta.board.dto.PostRequestDto;
import com.sparta.board.dto.PostResponseDto;
import com.sparta.board.dto.PostWithCommentsResponseDto;
import com.sparta.board.dto.ResponseDto;
import com.sparta.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 목록 조회
    @GetMapping("/api/posts")
    public List<PostWithCommentsResponseDto> getPosts(){ return postService.getPosts();}

    //게시글 작성 postmapping
    @PostMapping("/api/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.createPost(requestDto, request);
    }

    //선택한 게시글 조회
    @GetMapping("/api/post/{id}")
    public PostWithCommentsResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //선택한 게시글 수정
    @PutMapping("/api/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.updatePost(id,requestDto, request);
    }


    @DeleteMapping("/api/post/{id}") // 선택한 게시글 삭제
    public ResponseEntity<?> deleteArticle(@PathVariable Long id, HttpServletRequest request){
        boolean deleteResult = postService.deletePost(id, request);
        if(deleteResult == true) {
            String msg = "게시글 삭제 성공";
            return ResponseEntity.ok(new ResponseDto(msg, 200));
        }else{
            String msg = "게시글 삭제 실패";
            return ResponseEntity.ok(new ResponseDto(msg, 400));
        }

    }


}
