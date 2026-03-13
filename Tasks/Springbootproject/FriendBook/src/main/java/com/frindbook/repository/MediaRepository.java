package com.frindbook.repository;
import java.util.List;

import com.frindbook.entity.Media;
import com.frindbook.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {

    @EntityGraph(attributePaths = {"post"})
    List<Media> findByPost(Post post);
}