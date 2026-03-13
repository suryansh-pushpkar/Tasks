package com.frindbook.repository;

import com.frindbook.entity.Post;
import com.frindbook.entity.PostLike;
import com.frindbook.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<PostLike,Long> {

    boolean existsByUserAndPost(User user, Post post);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostLike l WHERE l.user = :user AND l.post = :post")
    void deleteByUserAndPost(User user, Post post);

    long countByPost(Post post);
}