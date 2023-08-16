package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    Order order;


    @BeforeEach
    public void init() {
        order = Order.builder().date(Date.from(Instant.now())).isCanceled(false).isDelivered(false).id(1L).build();
    }

    @Test
    void ItemRepo_SaveItem_ReturnUser() {
        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
    }

}