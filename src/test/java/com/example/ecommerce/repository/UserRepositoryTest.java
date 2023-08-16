package com.example.ecommerce.repository;

import com.example.ecommerce.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User user;


    @BeforeEach
    public void init() {
        user = User.builder().id(UUID.randomUUID()).email("asdasd@gmail.com").username("asd").password("asd").build();
    }

    @Test
    void ItemRepo_SaveItem_ReturnUser() {
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    void UserRepo_FindByUsername_ReturnUser() {
        userRepository.save(user);
        User foundUser = userRepository.findByUsername(user.getUsername()).get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    void UserRepo_ExistsByEmail_ReturnTrue() {
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    void UserRepo_ExistsByUsername_ReturnTrue() {
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertThat(exists).isTrue();
    }
}