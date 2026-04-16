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

        try {
            productRepository.save(product);
            response.put("message", "Product updated successfully");
        } catch (Exception e) {
            response.put("message", "Error updating product");
        }

        return response;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable String name, @PathVariable String category) {
        Map<String, Object> response = new HashMap<>();

        if ("null".equals(name) && "null".equals(category)) {
            response.put("products", productRepository.findAll());
        } else if ("null".equals(name)) {
            response.put("products", productRepository.findByCategory(category));
        } else if ("null".equals(category)) {
            response.put("products", productRepository.findProductBySubName(name));
        } else {
            response.put("products", productRepository.findProductBySubNameAndCategory(name, category));
        }

        return response;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());
        return response;
    }

    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable String category, @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        response.put("product", productRepository.findProductByCategory(category, storeid));
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable long id) {
        Map<String, String> response = new HashMap<>();

        boolean valid = serviceClass.validateProductId(id);
        if (!valid) {
            response.put("message", "Product not found");
            return response;
        }

        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        response.put("message", "Product deleted successfully");

        return response;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findProductBySubName(name));
        return response;
    }
}
