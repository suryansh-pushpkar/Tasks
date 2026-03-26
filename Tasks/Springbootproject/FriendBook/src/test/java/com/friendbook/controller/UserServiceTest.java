package com.friendbook.controller;

import com.friendbook.dto.UserDTO;
import com.friendbook.entity.User;
import com.friendbook.repository.UserRepository;
import com.friendbook.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

public class UserServiceTest {

    private MockMvc mockMvc;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerUser(){
        User user = new User();
        when(modelMapper.map(any(), any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        user.setFullName("Test User");
        user.setPassword("12345");
        user.setEmail("test@example.com");
        Assertions.assertNotNull(user);
    }


}
