package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CartService cartService;

    User user;

    Cart cart;

    @BeforeEach
    void init() {
        cart = Cart.builder().date(Date.from(Instant.now())).items(new ArrayList<>()).totalPrice(0).id(1L).build();
        user = User.builder().username("Asd").id(UUID.randomUUID()).build();

    }

    @Test
    void CartService_getCart_ReturnsCartDto() {
        CartDto response = cartService.getCart(user);

        assertThat(response).isEqualTo(modelMapper.map(user.getCart(),CartDto.class));
    }

    @Test
    void CartService_addItemsToCart_ReturnsCartDto() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").quantity(2).price(14).description("asdasdasd").build();
        Item item2 = Item.builder().outOfStock(false).id(2L).name("Item2").brand("Brand1").price(11).quantity(3).description("asdasdasd").build();
        Item item3 = Item.builder().outOfStock(false).id(3L).name("Item3").brand("Brand2").price(10).quantity(1).description("asdasdasd").build();

        ItemDto itemDto = ItemDto.builder().outOfStock(false).id(1L).build();
        ItemDto itemDto2 = ItemDto.builder().outOfStock(false).id(2L).build();
        ItemDto itemDto3 = ItemDto.builder().outOfStock(false).id(3L).build();

        List<Item> items = List.of(item,item2,item3);
        List<ItemDto> itemDtos = List.of(itemDto, itemDto2, itemDto3);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(itemRepository.findById(3L)).thenReturn(Optional.of(item3));


        CartDto response = cartService.addItemsToCart(user,itemDtos);


        assertThat(response.getItems()).containsAll(items);

    }

    @Test
    void CartService_addItemsToCart_ReturnsCartDto2() {

        ItemDto itemDto = ItemDto.builder().outOfStock(false).id(1L).build();
        ItemDto itemDto2 = ItemDto.builder().outOfStock(false).id(2L).build();
        ItemDto itemDto3 = ItemDto.builder().outOfStock(false).id(3L).build();

        List<ItemDto> itemDtos = List.of(itemDto, itemDto2, itemDto3);

        CartDto response = cartService.addItemsToCart(null,itemDtos);

        assertThat(response).isNull();

    }

    @Test
    void CartService_removeItemFromCart_ReturnsCartDto() {
        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").quantity(2).price(14).description("asdasdasd").build();
        ItemDto itemDto = ItemDto.builder().outOfStock(false).id(1L).build();


        cart.getItems().add(item);
        user.setCart(cart);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));


        CartDto response = cartService.removeItemFromCart(user,itemDto);

        assertThat(response.getItems()).doesNotContain(item);

    }

    @Test
    void CartService_removeItemFromCart_ReturnsNull() {
        Item item = Item.builder().build();
        ItemDto itemDto = ItemDto.builder().id(1L).build();

        user.setCart(cart);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));


        CartDto response = cartService.removeItemFromCart(user,itemDto);

        assertThat(response).isNull();

    }

    @Test
    void CartService_isCartExpired_ReturnsTrue() {
        cart.setExpDate(Date.from(Instant.now().minusSeconds(120)));
        user.setCart(cart);

        boolean response = cartService.isCartExpired(user);

        assertThat(response).isTrue();
    }

    @Test
    void CartService_isCartExpired_ReturnsFalse() {
        user.setCart(null);

        boolean response = cartService.isCartExpired(user);

        assertThat(response).isFalse();
    }

    @Test
    void CartService_isCartExpired_ReturnsFalse2() {
        cart.setExpDate(Date.from(Instant.now().plusSeconds(120)));
        user.setCart(cart);

        boolean response = cartService.isCartExpired(user);

        assertThat(response).isFalse();
    }
}