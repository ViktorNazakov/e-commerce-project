package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> addOrder(@AuthenticationPrincipal String username, @RequestBody List<Item> items) {
        User user = userService.findUserByUsername(username);
        OrderDto orderDto = orderService.saveOrder(user,items);
        if (orderDto == null) {
            return new ResponseEntity<>("Insufficient quantity of chosen item.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
    @PatchMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderDto orderDto) {
        boolean canceled = orderService.cancelOrder(orderDto);
        if (canceled) {
            return new ResponseEntity<>("Invalid order ID", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Order canceled", HttpStatus.OK);
    }

    @PatchMapping("/deliver")
    public ResponseEntity<String> deliverOrder(@RequestBody OrderDto orderDto) {
        boolean delivered = orderService.deliverOrder(orderDto);
        if (delivered) {
            return new ResponseEntity<>("Invalid order ID", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Order delivered", HttpStatus.OK);
    }

}
