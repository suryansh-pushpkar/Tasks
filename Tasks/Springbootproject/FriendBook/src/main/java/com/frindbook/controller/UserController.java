package com.frindbook.controller;

import com.frindbook.dto.SignupResponse;
import com.frindbook.dto.UserDTO;
import com.frindbook.utility.CaptchaUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frindbook.entity.User;
import com.frindbook.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaUtil captchaUtility;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody UserDTO dto) {

        boolean captchaVerified = captchaUtility.verifyCaptcha(dto.getCaptchaToken());
        if (!captchaVerified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(false, "Something Went Wrong!\n\t Please try again later ."));
        User user = modelMapper.map(dto, User.class);

        UserDTO ok = userService.registerUser(user);
        if (ok != null) {
            return ResponseEntity.ok(new SignupResponse(true, "Signed up successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SignupResponse(false, "User with this Email address already exists."));
        }
    }
}