package com.friendbook.repository;

import com.friendbook.entity.Post;
import com.friendbook.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserOrderByCreatedAtDesc(User user);

    List<Post> findByUserInOrderByCreatedAtDesc(Collection<User> users);
}
