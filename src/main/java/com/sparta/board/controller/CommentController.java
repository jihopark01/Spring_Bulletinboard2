package com.sparta.board.controller;


import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.ResponseDto;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    //댓글 작성
     @PostMapping("/api/post/{id}/comments")
      public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
         return commentService.createComment(id, requestDto, request);
     }

    //댓글 수정
    @PutMapping("/api/post/{id}/comments/{commentId}")
     public CommentResponseDto updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
         return commentService.updateComment(id,commentId,requestDto,request);
    }

    //댓글 삭제
    @DeleteMapping("/api/post/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long commentId,HttpServletRequest request ){
        boolean result = commentService.deleteComment(id,commentId,request);
        if(result){
            String msg = "댓글 삭제 성공";
            return ResponseEntity.ok(new ResponseDto(msg, 200));
        }else{
            String msg = "댓글 삭제 실패";
            return ResponseEntity.ok(new ResponseDto(msg, 400));
        }
    }


}
