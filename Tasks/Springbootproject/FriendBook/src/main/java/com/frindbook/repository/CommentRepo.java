package com.frindbook.repository;

import com.frindbook.entity.Comment;
import com.frindbook.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    Slice<Comment> findByPostOrderByCreatedAtAsc(Post post, Pageable pageable);

}
