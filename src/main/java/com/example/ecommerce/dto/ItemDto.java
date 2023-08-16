package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Review;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    private String name;

    private Integer price;

    private Integer quantity;

    private String brand;

    private String description;

    private boolean outOfStock;

    private List<Review> reviewList;
}
