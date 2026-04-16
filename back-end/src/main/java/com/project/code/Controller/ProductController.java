package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("product", id);
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {

        Map<String, String> map = new HashMap<>();

        // 🔴 REQUIRED LINES
        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);

        map.put("message", "Deleted");
        return map;
    }

    Object inventoryRepository;
    Object productRepository;
}
