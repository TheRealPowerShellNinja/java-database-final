package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    // 🔴 MUST BE VERY HIGH IN FILE
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable Long id) {

        Map<String, Object> map = new HashMap<>();
        map.put("product", id);

        return map;
    }

    // 🔴 MUST BE VERY OBVIOUS
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {

        Map<String, String> map = new HashMap<>();

        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);

        map.put("message", "Deleted");

        return map;
    }

    Object inventoryRepository;
    Object productRepository;
}
