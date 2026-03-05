package com.test.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByMai(String mail);
}
