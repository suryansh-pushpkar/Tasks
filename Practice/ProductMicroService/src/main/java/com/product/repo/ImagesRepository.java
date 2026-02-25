package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Images;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Integer>{

}
