package com.example.ecommerce.controller;

import com.example.ecommerce.config.JWTUtils;
import com.example.ecommerce.dto.AuthResponseDto;
import com.example.ecommerce.dto.LoginDto;
import com.example.ecommerce.dto.RegisterDto;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class RegisterLoginController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        // Check if provided username and email are valid
        if(!userService.checkIfUsernameAndEmailAreValid(registerDto.getUsername(),registerDto.getEmail())) {
            return new ResponseEntity<>("Username or email not valid", HttpStatus.BAD_REQUEST);
        }

        // Create a userDto object to check if it was saved
        UserDto user = userService.saveUser(registerDto);
        if (user == null) {
            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {

        // Create authentication with the passed username and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        // Set authentication with the created one
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findUserByUsername(loginDto.getUsername());

        // Generate token
        String token = jwtUtils.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponseDto(token, loginDto.getUsername(), user.getRoles().get(0).getName()), HttpStatus.OK);
    }

}
