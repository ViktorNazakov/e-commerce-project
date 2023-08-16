package com.example.ecommerce.service;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void CustomUserDetailsService_loadUserByUsername_ReturnsUserDetails() {
        User user = User.builder().username("asd").id(UUID.randomUUID()).roles(new ArrayList<>()).password("asdasdasd").build();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        UserDetails response = customUserDetailsService.loadUserByUsername(user.getUsername());

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
    }

}