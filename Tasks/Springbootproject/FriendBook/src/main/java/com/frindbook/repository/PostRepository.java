package com.frindbook.repository;

import com.frindbook.entity.Post;
import com.frindbook.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {

        @EntityGraph(attributePaths = {"user"})
        Slice<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

        @EntityGraph(attributePaths = {"user"})
        Slice<Post> findByUserInOrderByCreatedAtDesc(Collection<User> users, Pageable pageable);

        @EntityGraph(attributePaths = {"user"})
        Slice<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    }
