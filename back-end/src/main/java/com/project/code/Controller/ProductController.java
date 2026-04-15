package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
    
        try {
            boolean valid = serviceClass.validateProduct(product);
            if (valid) {
                productRepository.save(product);
                response.put("message", "Product added successfully");
            } else {
                response.put("message", "Product already exists");
            }
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Error adding product");
        }
    
        return response;
    }

    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        Product product = productRepository.findById(id);
        response.put("products", product);
        return response;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
    
        productRepository.save(product);
        response.put("message", "Product updated successfully");
    
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable long id) {
        Map<String, String> response = new HashMap<>();
    
        Product product = productRepository.findById(id);
        if (product != null) {
            productRepository.delete(product);
            response.put("message", "Product deleted successfully");
        } else {
            response.put("message", "Product not found");
        }
    
        return response;
    }
}