package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

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
    void OrderController_addOrder_ReturnsOrderDtoStatusOk() throws Exception{
        Item item = Item.builder().build();
        OrderDto orderDto = OrderDto.builder().build();

        List<Item> items = List.of(item);

        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(orderService.saveOrder(any(User.class),anyList())).willReturn(orderDto);


        ResultActions response = mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void OrderController_addOrder_ReturnsInsufficientQuantityStatusBadRequest() throws Exception{
        Item item = Item.builder().build();

        List<Item> items = List.of(item);

        given(userService.findUserByUsername(any(String.class))).willReturn(user);
        given(orderService.saveOrder(any(User.class),anyList())).willReturn(null);


        ResultActions response = mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void OrderController_cancelOrder_ReturnsOrderCanceledStatusOk() throws Exception {
        OrderDto orderDto = OrderDto.builder().build();

        given(orderService.cancelOrder(any(OrderDto.class))).willReturn(false);


        ResultActions response = mockMvc.perform(patch("/order/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order canceled"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void OrderController_cancelOrder_ReturnsInvalidOrderIdStatusBadRequest() throws Exception {
        OrderDto orderDto = OrderDto.builder().build();

        given(orderService.cancelOrder(any(OrderDto.class))).willReturn(true);


        ResultActions response = mockMvc.perform(patch("/order/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid order ID"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void OrderController_deliverOrder_ReturnsOrderDeliveredStatusOk() throws Exception {
        OrderDto orderDto = OrderDto.builder().build();

        given(orderService.deliverOrder(any(OrderDto.class))).willReturn(false);


        ResultActions response = mockMvc.perform(patch("/order/deliver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order delivered"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void OrderController_deliverOrder_ReturnsInvalidOrderIdStatusBadRequest() throws Exception {
        OrderDto orderDto = OrderDto.builder().build();

        given(orderService.deliverOrder(any(OrderDto.class))).willReturn(true);


        ResultActions response = mockMvc.perform(patch("/order/deliver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid order ID"))
                .andDo(MockMvcResultHandlers.print());
    }
}