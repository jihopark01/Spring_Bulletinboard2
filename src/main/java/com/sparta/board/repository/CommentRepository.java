package com.sparta.board.repository;

import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.id = :id and c.user = :userId ")
    Optional<Comment> findByIdAndUserId(@Param("id")Long id, @Param("userId")User user);

    boolean existsByUser(User user);

    Optional<Comment> findById(Long id);
}
