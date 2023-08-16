package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public CartDto getCart(User user) {
        return modelMapper.map(user.getCart(),CartDto.class);
    }


    public CartDto addItemsToCart(User user, List<ItemDto> itemIds) {
        List<Item> items = new ArrayList<>();
        if (user == null) {
            return null;
        }
        for (ItemDto i:
             itemIds) {
            Optional<Item> item = itemRepository.findById(i.getId());
            item.ifPresent(items::add);
        }
        if (user.getCart() == null) {
            Cart cart = Cart.builder().date(Date.from(Instant.now())).expDate(Date.from(Instant.now().plus(Duration.ofHours(1)))).totalPrice(0).items(new ArrayList<>()).build();
            cart.calculateTotalPrice();
            user.setCart(cart);
        }
        for (Item i:
             items) {
            if(!user.getCart().getItems().contains(i)) {
                user.getCart().getItems().add(i);
            }
        }
        user.getCart().calculateTotalPrice();
        userRepository.save(user);
        return CartDto.builder().date(user.getCart().getDate())
                .items(user.getCart().getItems())
                .expDate(user.getCart().getExpDate())
                .totalPrice(user.getCart().getTotalPrice()).build();
    }

    public CartDto removeItemFromCart(User user, ItemDto itemId) {
        Cart cart = user.getCart();
        Optional<Item> item = itemRepository.findById(itemId.getId());
        if (item.isEmpty() || cart.getItems().isEmpty()) {
            return null;
        }
        user.setCart(cart);
        user.getCart().calculateTotalPrice();
        userRepository.save(user);
        return cart.getItems().remove(item.get()) ?
                CartDto.builder().date(user.getCart().getDate())
                        .items(user.getCart().getItems())
                        .expDate(user.getCart().getExpDate())
                        .totalPrice(user.getCart().getTotalPrice()).build() : null;
    }

    public boolean isCartExpired(User user) {
        Cart cart = user.getCart();
        if (cart == null) {
            return false;
        }
        if (cart.getExpDate().after(Date.from(Instant.now()))) {
            return false;
        }

        deleteExpiredCart(user);

        return true;
    }

    private void deleteExpiredCart(User user) {
        user.setCart(null);
        userRepository.save(user);
    }
}
