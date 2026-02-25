package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Reviews;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Integer>{

}
