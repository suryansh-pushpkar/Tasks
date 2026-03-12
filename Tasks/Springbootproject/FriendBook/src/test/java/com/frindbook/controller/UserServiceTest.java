package com.frindbook.controller;

import com.frindbook.dto.UserDTO;
import com.frindbook.entity.User;
import com.frindbook.repository.UserRepository;
import com.frindbook.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

//        When(userRepository.save(hmentMatchers.anyString()))

        User user = new User();
        when(modelMapper.map(any(), any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        user.setFullName("Test User");
        user.setPassword("12345");
        user.setEmail("test@example.com");
        Assertions.assertNotNull(user);
    }


}
