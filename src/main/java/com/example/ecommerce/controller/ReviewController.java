package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ReviewDto;
import com.example.ecommerce.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{id}/reviews")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ReviewController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> addReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        ReviewDto review = itemService.addReview(id, reviewDto);
        if (review == null) {
            return new ResponseEntity<>("Invalid item id or review information", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {
        if (!itemService.deleteReview(id,reviewId)) {
            return new ResponseEntity<>("Review or item not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Review deleted",HttpStatus.OK);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> editReview(@PathVariable Long id, @PathVariable Long reviewId, @RequestBody ReviewDto reviewDto){
        ReviewDto review = itemService.editReview(id,reviewId,reviewDto);
        if (review == null) {
            return new ResponseEntity<>("Review or item not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(review,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllReviewsForItem(@PathVariable Long id) {
        List<ReviewDto> reviews = itemService.getReviewsForItem(id);
        if (reviews == null) {
            return new ResponseEntity<>("Item not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }
}
