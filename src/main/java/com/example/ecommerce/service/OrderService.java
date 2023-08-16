package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final ModelMapper modelMapper;

    public OrderDto saveOrder(User user ,List<Item> items) {
        Order order = Order.builder().date(Date.from(Instant.now())).isDelivered(false).isCanceled(false).build();
        List<OrderItem> orderItems = new ArrayList<>();
        for (Item item:
             items) {

            OrderItem orderItem = new OrderItem();
            Item itemInShop = itemRepository.findById(item.getId()).get();
            // make a check for quantity
            if (item.getQuantity() <= itemInShop.getQuantity()) {
                orderItem.setOrder(order);
                orderItem.setItem(item);
                orderItems.add(orderItem);
                itemInShop.setQuantity(itemInShop.getQuantity() - item.getQuantity());
                itemRepository.save(itemInShop);
            }
            // return null if quantity is not enough for chosen item
            else return null;


        }
        order.setOrderItems(orderItems);
        order.setAddress(user.getAddress());

        orderRepository.save(order);

        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }
        user.getOrders().add(order);
        userRepository.save(user);

        return OrderDto.builder()
                .id(order.getId())
                .address(order.getAddress())
                .isCanceled(order.isCanceled())
                .isDelivered(order.isDelivered())
                .build();

    }

    public boolean cancelOrder(OrderDto orderDto) {
        Optional<Order> order = orderRepository.findById(orderDto.getId());
        if (order.isEmpty()) {
            return false;
        }
        order.get().setCanceled(true);
        orderRepository.save(order.get());
        return true;
    }

    public boolean deliverOrder(OrderDto orderDto) {
        Optional<Order> order = orderRepository.findById(orderDto.getId());
        if (order.isEmpty()) {
            return false;
        }
        order.get().setDelivered(true);
        orderRepository.save(order.get());
        return true;
    }
}
