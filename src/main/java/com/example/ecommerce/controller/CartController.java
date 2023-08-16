package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> addItemsToCart(@AuthenticationPrincipal String username, @RequestBody List<ItemDto> items) {

        User user = userService.findUserByUsername(username);

        if (cartService.isCartExpired(user)) {
            return new ResponseEntity<>("Cart is expired", HttpStatus.BAD_REQUEST);
        }


        CartDto cart = cartService.addItemsToCart(user,items);
        if (cart == null) {
            return new ResponseEntity<>("Invalid information", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> removeItemFromCart(@AuthenticationPrincipal String username, @RequestBody ItemDto itemId) {

        User user = userService.findUserByUsername(username);

        if (cartService.isCartExpired(user)) {
            return new ResponseEntity<>("Cart is expired", HttpStatus.BAD_REQUEST);
        }

        CartDto cart = cartService.removeItemFromCart(user,itemId);
        if (cart == null) {
            return new ResponseEntity<>("Invalid information", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal String username) {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        if (cartService.isCartExpired(user)) {
            return new ResponseEntity<>("Cart is expired", HttpStatus.BAD_REQUEST);
        }

        CartDto cart = cartService.getCart(user);
        if (cart == null) {
            return new ResponseEntity<>("Invalid information", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
