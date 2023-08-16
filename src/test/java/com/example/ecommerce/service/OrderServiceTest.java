package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private OrderService orderService;
    @Test
    void OrderService_saveOrder_ReturnsOrderDto() {
        User user = User.builder().build();

        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").quantity(2).price(14).description("asdasdasd").build();
        Item item2 = Item.builder().outOfStock(false).id(2L).name("Item2").brand("Brand1").price(11).quantity(3).description("asdasdasd").build();
        Item item3 = Item.builder().outOfStock(false).id(3L).name("Item3").brand("Brand2").price(10).quantity(1).description("asdasdasd").build();

        List<Item> items = List.of(item,item2,item3);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(itemRepository.findById(3L)).thenReturn(Optional.of(item3));

        OrderDto response = orderService.saveOrder(user, items);

        assertThat(response.isCanceled()).isFalse();
        assertThat(response.isDelivered()).isFalse();
    }

    @Test
    void OrderService_saveOrder_ReturnsNull() {
        User user = User.builder().build();

        Item item = Item.builder().outOfStock(false).id(1L).name("Item1").brand("Brand1").quantity(2).price(14).description("asdasdasd").build();
        Item item2 = Item.builder().outOfStock(false).id(2L).name("Item2").brand("Brand1").price(11).quantity(3).description("asdasdasd").build();
        Item item3 = Item.builder().outOfStock(false).id(3L).name("Item3").brand("Brand2").price(10).quantity(1).description("asdasdasd").build();
        Item item4 = Item.builder().outOfStock(false).id(4L).name("Item4").brand("Brand2").price(10).quantity(0).description("asdasdasd").build();


        List<Item> items = List.of(item,item2,item3);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(itemRepository.findById(3L)).thenReturn(Optional.of(item4));

        OrderDto response = orderService.saveOrder(user, items);

        assertThat(response).isNull();
    }

    @Test
    void OrderService_cancelOrder_ReturnsTrue() {
        Order order = Order.builder().build();
        OrderDto orderDto = OrderDto.builder().id(1L).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));

        boolean response = orderService.cancelOrder(orderDto);

        assertThat(response).isTrue();
    }

    @Test
    void OrderService_cancelOrder_ReturnsFalse() {
        OrderDto orderDto = OrderDto.builder().id(1L).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        boolean response = orderService.cancelOrder(orderDto);

        assertThat(response).isFalse();
    }

    @Test
    void OrderService_deliverOrder_ReturnsTrue() {
        Order order = Order.builder().build();
        OrderDto orderDto = OrderDto.builder().id(1L).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));

        boolean response = orderService.deliverOrder(orderDto);

        assertThat(response).isTrue();
    }

    @Test
    void OrderService_deliverOrder_ReturnsFalse() {
        OrderDto orderDto = OrderDto.builder().id(1L).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        boolean response = orderService.deliverOrder(orderDto);

        assertThat(response).isFalse();
    }
}