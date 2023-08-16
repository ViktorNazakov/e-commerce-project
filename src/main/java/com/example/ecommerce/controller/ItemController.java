package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody ItemDto item) {
        ItemDto itemDto = itemService.addItem(item);
        if(itemDto == null) {
            return new ResponseEntity<>("Item already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemDto,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        ItemDto item = itemService.editItem(id, itemDto);
        if (item == null) {
            return new ResponseEntity<>("Not valid item id", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        if (!itemService.deleteItem(id)) {
            return new ResponseEntity<>("Item not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Item deleted", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {
        ItemDto item = itemService.getItem(id);
        if (item == null) {
            return new ResponseEntity<>("Item not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(item,HttpStatus.OK);
    }
}
