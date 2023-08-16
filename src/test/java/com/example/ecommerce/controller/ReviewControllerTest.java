package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ReviewDto;
import com.example.ecommerce.service.ItemService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ReviewController_addReview_ReturnsReviewDtoStatusOk() throws Exception {
        ReviewDto reviewDto = ReviewDto.builder().description("asd").numberOfStars(2).build();

        given(itemService.addReview(any(Long.class),any(ReviewDto.class))).willReturn(reviewDto);

        ResultActions response = mockMvc.perform(post("/items/{id}/reviews",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(reviewDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStars", CoreMatchers.is(reviewDto.getNumberOfStars())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_addReview_ReturnsInvalidIdOrInfoStatusBadRequest() throws Exception {
        ReviewDto reviewDto = ReviewDto.builder().description("asd").numberOfStars(2).build();

        given(itemService.addReview(any(Long.class),any(ReviewDto.class))).willReturn(null);

        ResultActions response = mockMvc.perform(post("/items/{id}/reviews",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_deleteReview_ReturnsDeletedStatusOk() throws Exception {

        given(itemService.deleteReview(any(Long.class),any(Long.class))).willReturn(true);

        ResultActions response = mockMvc.perform(delete("/items/{id}/reviews/{reviewId}",1L,1L));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Review deleted"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_deleteReview_ReturnsNotFoundStatusBadRequest() throws Exception {

        given(itemService.deleteReview(any(Long.class),any(Long.class))).willReturn(false);

        ResultActions response = mockMvc.perform(delete("/items/{id}/reviews/{reviewId}",1L,1L));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Review or item not found"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_editReview_ReturnsReviewDtoStatusBadRequest() throws Exception {

        ReviewDto reviewDto = ReviewDto.builder().description("asd").numberOfStars(2).build();

        given(itemService.editReview(any(Long.class),any(Long.class),any(ReviewDto.class))).willReturn(reviewDto);

        ResultActions response = mockMvc.perform(patch("/items/{id}/reviews/{reviewId}",1L,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(reviewDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStars", CoreMatchers.is(reviewDto.getNumberOfStars())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_editReview_ReturnsNotFoundStatusBadRequest() throws Exception {

        ReviewDto reviewDto = ReviewDto.builder().description("asd").numberOfStars(2).build();

        given(itemService.editReview(any(Long.class),any(Long.class),any(ReviewDto.class))).willReturn(null);

        ResultActions response = mockMvc.perform(patch("/items/{id}/reviews/{reviewId}",1L,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Review or item not found"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_getAllReviewsForItem_ReturnsReviewDtoStatusBadRequest() throws Exception {

        ReviewDto reviewDto = ReviewDto.builder().description("asd").numberOfStars(2).build();
        ReviewDto reviewDto2 = ReviewDto.builder().description("asd").numberOfStars(2).build();

        List<ReviewDto> reviews = List.of(reviewDto, reviewDto2);


        given(itemService.getReviewsForItem(any(Long.class))).willReturn(reviews);

        ResultActions response = mockMvc.perform(get("/items/{id}/reviews",1L));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(reviews.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void ReviewController_getAllReviewsForItem_ReturnsItemNotFoundStatusBadRequest() throws Exception {

        given(itemService.getReviewsForItem(any(Long.class))).willReturn(null);

        ResultActions response = mockMvc.perform(get("/items/{id}/reviews",1L));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Item not found"))
                .andDo(MockMvcResultHandlers.print());
    }
}