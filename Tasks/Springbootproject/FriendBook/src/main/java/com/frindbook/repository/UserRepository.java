package com.frindbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.frindbook.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User>findByUsernameAndPassword(String username, String password);

}
