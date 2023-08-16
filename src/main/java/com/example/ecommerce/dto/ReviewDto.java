package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private int numberOfStars;

    private String description;
}
