package com.frindbook.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.frindbook.dto.UserDTO;
import com.frindbook.entity.User;
import com.frindbook.repository.UserRepository;
import com.frindbook.utility.UsernameUtil;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}
	@Transactional
	public UserDTO registerUser(User user) {
		user.setUsername(UsernameUtil.generateUniqueUsername(user.getFullName(), userRepo));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User dbUser = userRepo.save(user);
		return modelMapper.map(dbUser, UserDTO.class);
	}

	public User authenticateUser(String email, String password) {
		Optional<User> userOpt = userRepo.findByEmail(email);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (passwordEncoder.matches(password, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
}
