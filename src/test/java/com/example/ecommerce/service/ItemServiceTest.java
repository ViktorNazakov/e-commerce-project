package com.example.ecommerce.service;

import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.dto.ReviewDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Review;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ItemService itemService;

    @Test
    void ItemService_getAllItems_ReturnListOfItem() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();
        Item item2 = Item.builder().outOfStock(false).id(1L).name("Item2").brand("Brand1").price(11).description("asdasdasd").build();
        Item item3 = Item.builder().outOfStock(false).id(1L).name("Item3").brand("Brand2").price(10).description("asdasdasd").build();

        List<Item> items = List.of(item,item2,item3);

        when(itemRepository.findAll()).thenReturn(items);

        List<Item> response = itemService.getAllItems();

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(items.size());
        assertThat(response).containsAll(items);
    }

    @Test
    void ItemService_addItem_ReturnsItemDto() {
        ItemDto itemDto = ItemDto.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto response = itemService.addItem(itemDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(itemDto.getName());
        assertThat(response.getId()).isEqualTo(itemDto.getId());

    }

    @Test
    void ItemService_addItem_ReturnsNull() {
        ItemDto itemDto = ItemDto.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();

        when(itemRepository.existsByName(any(String.class))).thenReturn(true);
        when(itemRepository.existsByBrand(any(String.class))).thenReturn(true);

        ItemDto response = itemService.addItem(itemDto);

        assertThat(response).isNull();

    }

    @Test
    void ItemService_deleteItem_ReturnsTrue() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        boolean response =  itemService.deleteItem(1L);

        assertThat(response).isTrue();
    }

    @Test
    void ItemService_deleteItem_ReturnsFalse() {

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        boolean response =  itemService.deleteItem(1L);

        assertThat(response).isFalse();
    }

    @Test
    void ItemService_getItem_ReturnsItemDto() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        ItemDto response =  itemService.getItem(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(item.getId());
        assertThat(response.getName()).isEqualTo(item.getName());
    }

    @Test
    void ItemService_getItem_ReturnsNull() {

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ItemDto response =  itemService.getItem(1L);

        assertThat(response).isNull();
    }

    @Test
    void ItemService_editItem_ReturnsItemDto() {
        ItemDto itemDto = ItemDto.builder().outOfStock(false).name("Item2").brand("Brand2").price(10).description("asdasdasd").build();
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").price(14).description("asdasdasd").build();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        ItemDto response = itemService.editItem(1L, itemDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(item.getName());
        assertThat(response.getBrand()).isEqualTo(item.getBrand());
        assertThat(response.getPrice()).isEqualTo(item.getPrice());
        assertThat(response.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void ItemService_editItem_ReturnsNull() {
        ItemDto itemDto = ItemDto.builder().outOfStock(false).name("Item2").brand("Brand2").price(10).description("asdasdasd").build();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ItemDto response = itemService.editItem(1L, itemDto);

        assertThat(response).isNull();
    }

    @Test
    void ItemService_addReview_ReturnsReviewDto() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").reviewList(new ArrayList<>()).price(14).description("asdasdasd").build();
        ReviewDto reviewDto = ReviewDto.builder().numberOfStars(2).description("asdasd").build();
        Review review = Review.builder().numberOfStars(2).description("asdasd").build();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDto response = itemService.addReview(1L, reviewDto);

        assertThat(response).isNotNull();
        assertThat(response.getDescription()).isEqualTo(reviewDto.getDescription());
        assertThat(response.getNumberOfStars()).isEqualTo(reviewDto.getNumberOfStars());

    }
    @Test
    void ItemService_addReview_ReturnsNull() {

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ReviewDto response = itemService.addReview(1L, null);

        assertThat(response).isNull();

    }

    @Test
    void ItemService_deleteReview_ReturnsTrue() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").reviewList(new ArrayList<>()).price(14).description("asdasdasd").build();
        Review review = Review.builder().review_id(1L).numberOfStars(2).description("asdasd").build();
        item.getReviewList().add(review);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review));

        boolean response = itemService.deleteReview(1L, 1L);

        assertThat(response).isTrue();
    }

    @Test
    void ItemService_deleteReview_ReturnsFalse() {

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        boolean response = itemService.deleteReview(1L, 1L);

        assertThat(response).isFalse();
    }
    @Test
    void ItemService_deleteReview_ReturnsFalse2() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").reviewList(new ArrayList<>()).price(14).description("asdasdasd").build();
        Review review = Review.builder().review_id(1L).numberOfStars(2).description("asdasd").build();
        Review review2 = Review.builder().review_id(2L).numberOfStars(2).description("asdasd").build();
        item.getReviewList().add(review2);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review));

        boolean response = itemService.deleteReview(1L, 1L);

        assertThat(response).isFalse();
    }


    @Test
    void ItemService_editReview_ReturnsReviewDto() {

        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").reviewList(new ArrayList<>()).price(14).description("asdasdasd").build();
        Review review = Review.builder().review_id(1L).numberOfStars(2).description("asdasd").build();
        ReviewDto reviewDto = ReviewDto.builder().numberOfStars(4).description("a").build();
        item.getReviewList().add(review);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review));

        ReviewDto response = itemService.editReview(1L, 1L,reviewDto);

        assertThat(response).isNotNull();
        assertThat(response.getNumberOfStars()).isEqualTo(review.getNumberOfStars());
        assertThat(response.getDescription()).isEqualTo(review.getDescription());
    }

    @Test
    void ItemService_editReview_ReturnsNull() {
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ReviewDto response = itemService.editReview(1L, 1L,null);

        assertThat(response).isNull();
    }

    @Test
    void ItemService_getReviewsForItem_ReturnsListOfReviews() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").reviewList(new ArrayList<>()).price(14).description("asdasdasd").build();
        Review review = Review.builder().review_id(1L).numberOfStars(2).description("asdasd").build();
        Review review2 = Review.builder().review_id(2L).numberOfStars(4).description("asd").build();
        List<Review> reviews = List.of(review,review2);
        item.getReviewList().addAll(reviews);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));

        List<ReviewDto> response = itemService.getReviewsForItem(1L);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(reviews.size());

    }

    @Test
    void ItemService_getReviewsForItem_ReturnsNull() {


        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        List<ReviewDto> response = itemService.getReviewsForItem(1L);

        assertThat(response).isNull();

    }
}