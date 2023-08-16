package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    Cart cart;


    @BeforeEach
    public void init() {
        cart = Cart.builder().items(new ArrayList<>()).date(Date.from(Instant.now())).totalPrice(10).id(1L).build();
    }

    @Test
    void ItemRepo_SaveItem_ReturnUser() {
        Cart savedOrder = cartRepository.save(cart);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getItems().size()).isEqualTo(cart.getItems().size());
    }

}