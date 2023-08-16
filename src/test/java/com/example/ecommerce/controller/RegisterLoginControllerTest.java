package com.example.ecommerce.controller;

import com.example.ecommerce.config.JWTUtils;
import com.example.ecommerce.dto.LoginDto;
import com.example.ecommerce.dto.RegisterDto;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@WebMvcTest(controllers = RegisterLoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RegisterLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void RegisterLoginController_register_ReturnsUserDtoStatusOK() throws Exception {
        RegisterDto registerDto = RegisterDto.builder().email("Ads_sd123@gmail.com").username("Asdsdasd").password("asdasdasd").build();
        UserDto userDto = UserDto.builder().email(registerDto.getEmail()).username(registerDto.getUsername()).build();

        given(userService.checkIfUsernameAndEmailAreValid(registerDto.getUsername(), registerDto.getEmail())).willReturn(true);
        given(userService.saveUser(any())).willReturn(userDto);

        ResultActions response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(registerDto.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(registerDto.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void RegisterLoginController_register_ReturnsInvalidEmailOrUsernameStatusBadRequest() throws Exception {
        RegisterDto registerDto = RegisterDto.builder().email("Ads_sd123@gmail.com").username("Asdsdasd").password("asdasdasd").build();

        given(userService.checkIfUsernameAndEmailAreValid(registerDto.getUsername(), registerDto.getEmail())).willReturn(false);

        ResultActions response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void RegisterLoginController_register_ReturnsUsernameIsTakenStatusBadRequest() throws Exception {
        RegisterDto registerDto = RegisterDto.builder().email("Ads_sd123@gmail.com").username("Asdsdasd").password("asdasdasd").build();

        given(userService.checkIfUsernameAndEmailAreValid(registerDto.getUsername(), registerDto.getEmail())).willReturn(true);
        given(userService.saveUser(any())).willReturn(null);


        ResultActions response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Username is taken"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void RegisterLoginController_login_ReturnsAuthResponseDtoStatusOK() throws Exception {
        LoginDto loginDto = LoginDto.builder().username("Asdsd").password("asdasd").build();
        User user = User.builder().roles(List.of(new Role(1, "USER"))).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword())))
                .willReturn(authentication);
        given(userService.findUserByUsername(loginDto.getUsername())).willReturn(user);
        given(jwtUtils.generateToken(authentication)).willReturn("token");

        ResultActions response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(loginDto.getUsername())))
                .andDo(MockMvcResultHandlers.print());
    }
}