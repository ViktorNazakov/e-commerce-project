package com.example.ecommerce.service;

import com.example.ecommerce.dto.RegisterDto;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final PasswordEncoder passwordEncoder;

    public UserDto saveUser(RegisterDto registerDto) {
        if (userRepo.existsByUsername(registerDto.getUsername()) || userRepo.existsByEmail(registerDto.getEmail())) {
            return null;
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setAddress(registerDto.getAddress());

        Role role = roleRepo.findByName("USER");
        user.setRoles(Collections.singletonList(role));


        User savedUser = userRepo.save(user);

        UserDto userDto = new UserDto();
        userDto.setUsername(savedUser.getUsername());
        userDto.setEmail(savedUser.getEmail());


        return userDto;
    }



    public User findUserByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }

    public boolean checkIfUsernameAndEmailAreValid(String username, String email) {

        // regexUsername checks for:
        //  1- ^ represents the starting character
        //  2- [A-Za-z] makes sure the starting character is in the lowercase or uppercase alphabet
        //  3- \\w{3, 29} - represents a check to make sure that the remaining items are word items,
        //  which includes the underscore, until it reaches the end and that is represented with $
        String regexUsername = "^[A-Za-z]\\w{3,29}$";
        // regexEmail checks for:
        //  1- A-Za-z characters
        //  2- 0-9 numbers
        //  3- email can contain . _ -
        //  4- the remaining characters are not allowed
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Check if username or email is empty
        // return false
        if (username == null || email == null) {
            return false;
        }

        // Compile the regex
        Pattern pU = Pattern.compile(regexUsername);
        Pattern pE = Pattern.compile(regexEmail);

        // finds matching between given username and regex
        Matcher mU = pU.matcher(username);
        // finds matching between given email and regex
        Matcher mE = pE.matcher(email);

        // Return if username and email
        // matched the regex
        return mU.matches() || mE.matches();

    }


}
