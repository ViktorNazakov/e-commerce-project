package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);

    boolean existsByBrand(String brand);
}
