package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer price;

    private Integer quantity;

    private String brand;

    private String description;

    private boolean outOfStock;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();



}
