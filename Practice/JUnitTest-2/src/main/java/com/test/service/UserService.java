package com.test.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.test.dto.UserDTO;
import com.test.entity.User;
import com.test.repo.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final ModelMapper mapper;
	public UserService(UserRepository userRepo, ModelMapper mapper) {
		this.mapper = mapper;
		this.userRepo = userRepo;
	}
	
	public ResponseEntity<?> saveUser(User user){
	 User dbUser = userRepo.save(user);
	 UserDTO dto = mapper.map(dbUser, UserDTO.class);
	 return ResponseEntity.ok(dto);
	}
	
}
