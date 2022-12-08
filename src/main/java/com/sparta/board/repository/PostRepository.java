package com.sparta.board.repository;

import com.sparta.board.entity.Post;
import com.sparta.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllMemoByOrderByModifiedAtDesc();
    Optional<Post> findByIdAndUser(Long id, User user);
    Boolean existsByIdAndUser(Long id,User user);

    Optional<Post> findById(Long id);
}
