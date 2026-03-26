package com.frindbook.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.frindbook.dto.UserDTO;
import com.frindbook.entity.User;
import com.frindbook.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	  @Transactional
	   public UserDTO registerUser(User user) {
		   
		   String encryptedPassword = passwordEncoder.encode(user.getPassword());
		   user.setPassword(encryptedPassword);
		   User dbUser =  userRepo.save(user);
		   UserDTO dto = new UserDTO();
		   dto.setEmail(dbUser.getEmail());
		   return dto;
	   }
	
}
