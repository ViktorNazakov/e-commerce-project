package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Item;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Date date;

    private Date expDate;

    private Integer totalPrice;

    private List<Item> items;
}
