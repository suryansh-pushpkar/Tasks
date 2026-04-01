package com.friendbook.controller;

import com.friendbook.dto.UserDTO;
import com.friendbook.entity.User;
import com.friendbook.repository.UserRepository;
import com.friendbook.service.UserService;
import com.friendbook.service.UsernameGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new UsernameGenerator(userRepository), new BCryptPasswordEncoder());
    }

    @Test
    public void registerUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("Test User");
        userDTO.setPassword("12345");
        userDTO.setEmail("test@example.com");
        userDTO.setCaptchaToken("token");

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setUserName("Testu123");

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUserName("Testu123")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(userDTO);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("Test User", savedUser.getFullName());
        Assertions.assertEquals("test@example.com", savedUser.getEmail());
        Assertions.assertEquals("Testu123", savedUser.getUserName());
        Assertions.assertNotEquals("12345", savedUser.getPassword());
        verify(userRepository).save(any(User.class));
    }
}
