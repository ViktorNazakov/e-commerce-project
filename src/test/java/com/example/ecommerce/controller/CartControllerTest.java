package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private Authentication authentication;

    private User user;

    @BeforeEach
    void init() {
        authentication = new UsernamePasswordAuthenticationToken("user","user");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        user = User.builder().username("user").build();
    }

    @Test
    void CartController_addItemsToCart_ReturnsCartDtoStatusOk() throws Exception {
        CartDto cartDto = CartDto.builder().build();
        ItemDto itemDto = ItemDto.builder().build();
        Item item = Item.builder().build();

        List<ItemDto> items = List.of(itemDto);
        cartDto.setItems(List.of(item));


        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(false);
        given(cartService.addItemsToCart(any(User.class),anyList())).willReturn(cartDto);

        ResultActions response = mockMvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()", CoreMatchers.is(items.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_addItemsToCart_ReturnsCartIsExpiredStatusBadRequest() throws Exception {
        CartDto cartDto = CartDto.builder().build();
        ItemDto itemDto = ItemDto.builder().build();
        Item item = Item.builder().build();

        List<ItemDto> items = List.of(itemDto);
        cartDto.setItems(List.of(item));


        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(true);

        ResultActions response = mockMvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_addItemsToCart_ReturnsInvalidInformationStatusBadRequest() throws Exception {
        CartDto cartDto = CartDto.builder().build();
        ItemDto itemDto = ItemDto.builder().build();
        Item item = Item.builder().build();

        List<ItemDto> items = List.of(itemDto);
        cartDto.setItems(List.of(item));


        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(false);
        given(cartService.addItemsToCart(any(User.class),anyList())).willReturn(null);

        ResultActions response = mockMvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_removeItemFromCart_ReturnsCartDtoStatusOk() throws Exception {
        CartDto cartDto = CartDto.builder().build();
        ItemDto itemDto = ItemDto.builder().build();
        Item item = Item.builder().build();

        List<ItemDto> items = List.of(itemDto);
        cartDto.setItems(List.of(item));


        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(false);
        given(cartService.removeItemFromCart(any(User.class),any(ItemDto.class))).willReturn(cartDto);

        ResultActions response = mockMvc.perform(delete("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()", CoreMatchers.is(items.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_removeItemFromCart_ReturnsCartIsExpiredStatusBadRequest() throws Exception {
        ItemDto itemDto = ItemDto.builder().build();

        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(true);

        ResultActions response = mockMvc.perform(delete("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_removeItemFromCart_ReturnsInvalidInformationStatusBadRequest() throws Exception {
        ItemDto itemDto = ItemDto.builder().build();

        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(false);
        given(cartService.removeItemFromCart(any(User.class),any(ItemDto.class))).willReturn(null);

        ResultActions response = mockMvc.perform(delete("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_getCart_ReturnsCartDtoStatusOk() throws Exception {
        CartDto cartDto = CartDto.builder().build();
        ItemDto itemDto = ItemDto.builder().build();
        Item item = Item.builder().build();

        List<ItemDto> items = List.of(itemDto);
        cartDto.setItems(List.of(item));


        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(false);
        given(cartService.getCart(any(User.class))).willReturn(cartDto);

        ResultActions response = mockMvc.perform(get("/cart"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()", CoreMatchers.is(items.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_getCart_ReturnsUserNotFoundStatusBadRequest() throws Exception {

        given(userService.findUserByUsername(any(String.class))).willReturn(null);

        ResultActions response = mockMvc.perform(get("/cart"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_getCart_ReturnsCartIsExpiredStatusBadRequest() throws Exception {

        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(true);

        ResultActions response = mockMvc.perform(get("/cart"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void CartController_getCart_ReturnsInvalidInformationStatusBadRequest() throws Exception {

        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(cartService.isCartExpired(any(User.class))).willReturn(false);
        given(cartService.getCart(any(User.class))).willReturn(null);

        ResultActions response = mockMvc.perform(get("/cart"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}