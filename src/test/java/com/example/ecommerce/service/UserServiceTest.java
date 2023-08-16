package com.example.ecommerce.service;

import com.example.ecommerce.dto.RegisterDto;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_saveUser_ReturnsUserDto() {
        User user = User.builder().id(UUID.randomUUID()).email("Ads_sd123@gmail.com").username("Aasdsdsd").password("asd").build();
        Role userRole = Role.builder().id(1).name("USER").build();
        RegisterDto registerDto = RegisterDto.builder().email(user.getEmail()).username(user.getUsername()).password(user.getPassword()).build();
        when(roleRepository.findByName("USER")).thenReturn(userRole);
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserDto response = userService.saveUser(registerDto);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
    }
    @Test
    public void UserService_saveUser_ReturnsNull() {
        User user = User.builder().id(UUID.randomUUID()).email("asd").username("a").password("asd").build();
        RegisterDto registerDto = RegisterDto.builder().email(user.getEmail()).username(user.getUsername()).password(user.getPassword()).build();

        UserDto response = userService.saveUser(registerDto);

        assertThat(response).isNull();
    }

    @Test
    void UserService_FindUserByUsername_ReturnUser() {
        User user = User.builder().id(UUID.randomUUID()).username("asd").password("asd").build();

        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));

        User response = userService.findUserByUsername(user.getUsername());

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(user.getUsername());

    }
}