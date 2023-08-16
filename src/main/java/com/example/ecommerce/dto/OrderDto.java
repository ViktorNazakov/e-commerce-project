package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.OrderItem;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private Date date;

    private boolean isCanceled;

    private boolean isDelivered;

    private Address address;

}
