package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Address;
import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String username;

    private String email;

    private String password;

    private Address address;
}
