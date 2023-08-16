package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Role;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepo;

    Role role;


    @BeforeEach
    public void init() {
        role = Role.builder().name("USER").build();
    }

    @Test
    public void RoleRepo_SaveRole_ReturnsRole() {

        Role savedRole = roleRepo.save(role);

        AssertionsForClassTypes.assertThat(savedRole).isNotNull();
        AssertionsForClassTypes.assertThat(savedRole.getName()).isEqualTo(role.getName());
        AssertionsForClassTypes.assertThat(savedRole.getId()).isEqualTo(role.getId());
    }

    @Test
    public void RoleRepo_FindByName_ReturnsRole() {

        roleRepo.save(role);

        Role foundRole = roleRepo.findByName(role.getName());

        AssertionsForClassTypes.assertThat(foundRole).isNotNull();
        AssertionsForClassTypes.assertThat(foundRole.getName()).isEqualTo(role.getName());
        AssertionsForClassTypes.assertThat(foundRole.getId()).isEqualTo(role.getId());
    }
}