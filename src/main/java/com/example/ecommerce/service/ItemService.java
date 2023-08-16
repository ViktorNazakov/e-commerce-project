package com.example.ecommerce.service;

import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.dto.ReviewDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Review;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ReviewRepository reviewRepository;

    private final ModelMapper modelMapper;

    public List<Item> getAllItems() {
        return itemRepository.findAll();

    }
    public ItemDto addItem(ItemDto itemDto) {
        if(itemRepository.existsByName(itemDto.getName()) && itemRepository.existsByBrand(itemDto.getBrand())) {
            return null;
        }

        Item item = Item.builder().name(itemDto.getName()).brand(itemDto.getBrand()).description(itemDto.getDescription()).price(itemDto.getPrice()).build();

        Item savedItem = itemRepository.save(item);

        return ItemDto.builder().id(savedItem.getId()).name(savedItem.getName()).brand(savedItem.getBrand()).description(savedItem.getDescription()).price(savedItem.getPrice()).build();


    }

    public boolean deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            return false;
        }
        itemRepository.delete(item.get());
        return true;
    }

    public ItemDto getItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if(item.isEmpty()) {
            return null;
        }

        return ItemDto.builder().id(item.get().getId()).name(item.get().getName()).brand(item.get().getBrand()).description(item.get().getDescription()).price(item.get().getPrice()).build();
    }

    public ItemDto editItem(Long id, ItemDto itemDto) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            return null;
        }
        updateItemIfNotNull(item.get(), itemDto.getBrand(), Item::setBrand);
        updateItemIfNotNull(item.get(), itemDto.getName(), Item::setName);
        updateItemIfNotNull(item.get(), itemDto.getPrice(), Item::setPrice);
        updateItemIfNotNull(item.get(), itemDto.getDescription(), Item::setDescription);

        itemRepository.save(item.get());

        return ItemDto.builder()
                .name(item.get().getName())
                .brand(item.get().getBrand())
                .description(item.get().getDescription())
                .price(item.get().getPrice())
                .build();
    }

    //BiConsumer is a functional interface
    private <T> void updateItemIfNotNull(Item item, T value, BiConsumer<Item, T> setter) {
        if (value != null) {
            setter.accept(item, value);
        }
    }

    public ReviewDto addReview(Long itemId, ReviewDto reviewDto) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (reviewDto == null || item.isEmpty()) {
            return null;
        }
        Review review = Review.builder().description(reviewDto.getDescription()).numberOfStars(reviewDto.getNumberOfStars()).build();

        Review savedReview = reviewRepository.save(review);
        item.get().getReviewList().add(review);
        itemRepository.save(item.get());

        return ReviewDto.builder().description(savedReview.getDescription()).numberOfStars(savedReview.getNumberOfStars()).build();
    }

    public boolean deleteReview(Long itemId, Long reviewId) {
        Optional<Item> item = itemRepository.findById(itemId);
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (item.isEmpty() || review.isEmpty()) {
            return false;
        }
        if(item.get().getReviewList().contains(review.get())) {
            item.get().getReviewList().remove(review.get());
            itemRepository.save(item.get());
            return true;
        }
        return false;
    }

    public ReviewDto editReview(Long itemId,Long reviewId, ReviewDto reviewDto) {
        Optional<Item> item = itemRepository.findById(itemId);
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (item.isEmpty() || review.isEmpty()) {
            return null;
        }
        modelMapper.map(reviewDto, review.get());
        reviewRepository.save(review.get());


        return ReviewDto.builder().description(review.get().getDescription()).numberOfStars(review.get().getNumberOfStars()).build();
    }

    public List<ReviewDto> getReviewsForItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty() || item.get().getReviewList().isEmpty()) {
            return null;
        }
        return item.map(value -> value.getReviewList()
                .stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList())).orElse(null);

    }
}
