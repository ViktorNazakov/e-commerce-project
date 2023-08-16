package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    Address address;


    @BeforeEach
    public void init() {
        address = Address.builder().country("asdasd").city("asd").id(1L).build();
    }

    @Test
    void ItemRepo_SaveItem_ReturnUser() {
        Address savedAddress = addressRepository.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getCity()).isEqualTo(address.getCity());
        assertThat(savedAddress.getCountry()).isEqualTo(address.getCountry());
    }

}