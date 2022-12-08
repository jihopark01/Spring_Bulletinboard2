package com.sparta.board.dto;

import com.sparta.board.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CommentResponseDto {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String comment;


    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.comment  = comment.getComment();
    }


}
