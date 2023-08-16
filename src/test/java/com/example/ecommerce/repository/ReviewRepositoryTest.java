package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    Review review;


    @BeforeEach
    public void init() {
        review = Review.builder().numberOfStars(3).review_id(1L).description("asdasd").build();
    }

    @Test
    void ItemRepo_SaveItem_ReturnUser() {
        Review savedReview = reviewRepository.save(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getReview_id()).isNotNull();
        assertThat(savedReview.getNumberOfStars()).isEqualTo(review.getNumberOfStars());
        assertThat(savedReview.getDescription()).isEqualTo(review.getDescription());

    }

}