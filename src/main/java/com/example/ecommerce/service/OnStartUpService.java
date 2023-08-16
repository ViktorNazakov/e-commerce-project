package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OnStartUpService {

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final ItemRepository itemRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${com.example.JWTpostgres.role.user.id}")
    private int USER_ROLE_ID;

    @Value("${com.example.JWTpostgres.role.user.name}")
    private String USER_ROLE_NAME;

    @Value("${com.example.JWTpostgres.role.admin.id}")
    private int ADMIN_ROLE_ID;

    @Value("${com.example.JWTpostgres.role.admin.name}")
    private String ADMIN_ROLE_NAME;

    @EventListener(ApplicationReadyEvent.class)
    public void saveUserRoles() {
        Role userRole = Role.builder().id(this.USER_ROLE_ID).name(this.USER_ROLE_NAME).build();
        Role adminRole = Role.builder().id(this.ADMIN_ROLE_ID).name(this.ADMIN_ROLE_NAME).build();
        List<Role> roleList = new ArrayList<>();
        roleList.add(userRole);
        roleList.add(adminRole);
        roleRepo.saveAll(roleList);

        Item item = Item.builder().name("Item1").brand("brand1").quantity(2).outOfStock(false).price(14).description("asdasdasd").reviewList(new ArrayList<>()).build();
        Item item2 = Item.builder().name("Item2").brand("brand2").quantity(5).outOfStock(false).price(12).description("asdasdasd").reviewList(new ArrayList<>()).build();
        Item item3 = Item.builder().name("Item3").brand("brand1").quantity(1).outOfStock(false).price(4).description("asdasdasd").reviewList(new ArrayList<>()).build();

        Item item4 = Item.builder().name("Item4").brand("brand1").quantity(10).outOfStock(false).price(14).description("asdasdasd").reviewList(new ArrayList<>()).build();

        itemRepository.saveAll(List.of(item,item2,item3,item4));


        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@admin.admin");
        admin.setAddress(Address.builder().city("City").country("Country").build());
        admin.setPassword(passwordEncoder.encode("admin"));
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@user.user");
        user.setPassword(passwordEncoder.encode("user"));

        admin.setRoles(Collections.singletonList(adminRole));
        user.setRoles(Collections.singletonList(userRole));

        userRepo.save(user);
        userRepo.save(admin);

    }
}
