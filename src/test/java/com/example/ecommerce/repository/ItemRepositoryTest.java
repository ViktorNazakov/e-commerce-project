package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    Item item;


    @BeforeEach
    public void init() {
        item = Item.builder().description("asdasd").brand("asdasd").name("Asd").id(1L).price(10).reviewList(new ArrayList<>()).build();
    }

    @Test
    void ItemRepo_SaveItem_ReturnItem() {
        Item savedItem = itemRepository.save(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isEqualTo(item.getId());
        assertThat(savedItem.getName()).isEqualTo(item.getName());
    }


    @Test
    void ItemRepo_ExistsByName_ReturnTrue() {
        itemRepository.save(item);

        boolean exists = itemRepository.existsByName("Asd");

        assertThat(exists).isTrue();
    }

    @Test
    void ItemRepo_ExistsByBrand_ReturnTrue() {
        itemRepository.save(item);

        boolean exists = itemRepository.existsByBrand("asdasd");

        assertThat(exists).isTrue();
    }
}