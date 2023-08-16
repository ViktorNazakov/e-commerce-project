package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;
    ItemDto itemDto;
    @BeforeEach
    void init() {
        itemDto = ItemDto.builder().id(1L).name("Item1").brand("brand1").build();
    }

    @Test
    void ItemController_addItem_ReturnsItemDtoStatusOk() throws Exception {
        given(itemService.addItem(any(ItemDto.class))).willReturn(itemDto);

        ResultActions response = mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(itemDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", CoreMatchers.is(itemDto.getBrand())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void ItemController_addItem_ReturnsItemAlreadyExistsStatusBadRequest() throws Exception {
        given(itemService.addItem(any(ItemDto.class))).willReturn(null);

        ResultActions response = mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void ItemController_getAllItems_ReturnsListOfItemsStatusOk() throws Exception {
        Item item1 = Item.builder().name("Item1").brand("brand1").build();
        Item item2 = Item.builder().name("Item2").brand("brand2").build();
        Item item3 = Item.builder().name("Item3").brand("brand3").build();

        List<Item> items = List.of(item1,item2,item3);

        given(itemService.getAllItems()).willReturn(items);

        ResultActions response = mockMvc.perform(get("/items/all"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(items.size())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void ItemController_editItem_ReturnsItemDtoStatusOk() throws Exception {
        ItemDto itemDtoChanged = ItemDto.builder().id(1L).name("Item1").brand("brand2").build();

        given(itemService.editItem(any(Long.class),any(ItemDto.class))).willReturn(itemDtoChanged);

        ResultActions response = mockMvc.perform(patch("/items/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(itemDtoChanged.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", CoreMatchers.is(itemDtoChanged.getBrand())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void ItemController_editItem_ReturnsNotValidIdStatusBadRequest() throws Exception {
        given(itemService.editItem(any(Long.class),any(ItemDto.class))).willReturn(null);

        ResultActions response = mockMvc.perform(patch("/items/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void ItemController_deleteItem_ReturnsItemDeletedStatusOk() throws Exception {

        given(itemService.deleteItem(any(Long.class))).willReturn(true);

        ResultActions response = mockMvc.perform(delete("/items/{id}", 1));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ItemController_deleteItem_ReturnsItemNotFoundStatusBadRequest() throws Exception {

        given(itemService.deleteItem(any(Long.class))).willReturn(false);

        ResultActions response = mockMvc.perform(delete("/items/{id}", 1));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ItemController_getItem_ReturnsItemDtoStatusBadRequest() throws Exception {
        given(itemService.getItem(any(Long.class))).willReturn(itemDto);

        ResultActions response = mockMvc.perform(get("/items/{id}", 1));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ItemController_getItem_ReturnsItemNotFoundStatusBadRequest() throws Exception {

        given(itemService.getItem(any(Long.class))).willReturn(null);

        ResultActions response = mockMvc.perform(get("/items/{id}", 1));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}