package com.friendbook.repository;

import com.friendbook.entity.Post;
import com.friendbook.entity.PostLike;
import com.friendbook.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
