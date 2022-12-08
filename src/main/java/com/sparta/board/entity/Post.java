package com.sparta.board.entity;


import com.sparta.board.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column (nullable = false)
    private String content;

    @ManyToOne //cascade, fetch, optional
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @OrderBy("id asc") // 정렬
    private List<Comment> comments;


   public Post(PostRequestDto requestDto, User user){
       this.title = requestDto.getTitle();
       this.content = requestDto.getContent();
       this.user = user;

   }


    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
