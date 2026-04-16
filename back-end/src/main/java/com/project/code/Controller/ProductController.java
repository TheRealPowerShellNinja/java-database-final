package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();

        try {
            boolean valid = serviceClass.validateProduct(product);
            if (valid) {
                productRepository.save(product);
                response.put("message", "Product added successfully");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("message", "Product already exists");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Error adding product");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Map<String, Object>> getProductbyId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Product product = productRepository.findById(id);
        if (product != null) {
            response.put("products", product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "No data available");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();

        try {
            productRepository.save(product);
            response.put("message", "Product updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error updating product");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/category/{name}/{category}")
    public ResponseEntity<Map<String, Object>> filterbyCategoryProduct(@PathVariable String name,
                                                                       @PathVariable String category) {
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

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listProduct() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filter/{category}/{storeid}")
    public ResponseEntity<Map<String, Object>> getProductbyCategoryAndStoreId(@PathVariable String category,
                                                                              @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        response.put("product", productRepository.findProductByCategory(category, storeid));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        boolean valid = serviceClass.validateProductId(id);
        if (!valid) {
            response.put("message", "Product not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        response.put("message", "Product deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<Map<String, Object>> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findProductBySubName(name));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
