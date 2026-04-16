package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
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
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping
    public ResponseEntity<Map<String, String>> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            boolean valid = serviceClass.validateProductId(request.getProduct().getId());
            if (!valid) {
                response.put("message", "No data available");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            request.getInventory().setProduct(request.getProduct());
            Inventory inventory = serviceClass.getInventoryId(request.getInventory());

            if (inventory != null) {
                inventory.setStockLevel(request.getInventory().getStockLevel());
                productRepository.save(request.getProduct());
                inventoryRepository.save(inventory);
                response.put("message", "Successfully updated product");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "No data available");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (DataIntegrityViolationException e) {
            response.put("message", "Error updating product");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("message", "Error updating product");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> response = new HashMap<>();

        try {
            boolean valid = serviceClass.validateInventory(inventory);
            if (!valid) {
                response.put("message", "Data already present");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                inventoryRepository.save(inventory);
                response.put("message", "Data saved successfully");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Error saving inventory");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("message", "Error saving inventory");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{storeid}")
    public ResponseEntity<Map<String, Object>> getAllProducts(@PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findProductsByStoreId(storeid));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("filter/{category}/{name}/{storeid}")
    public ResponseEntity<Map<String, Object>> getProductName(@PathVariable String category,
                                                              @PathVariable String name,
                                                              @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();

        if ("null".equals(category)) {
            response.put("product", productRepository.findByNameLike(storeid, name));
        } else if ("null".equals(name)) {
            response.put("product", productRepository.findByCategoryAndStoreId(storeid, category));
        } else {
            response.put("product", productRepository.findByNameAndCategory(storeid, name, category));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("search/{name}/{storeId}")
    public ResponseEntity<Map<String, Object>> searchProduct(@PathVariable String name,
                                                             @PathVariable Long storeId) {
        Map<String, Object> response = new HashMap<>();
        response.put("product", productRepository.findByNameLike(storeId, name));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> removeProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        boolean valid = serviceClass.validateProductId(id);
        if (!valid) {
            response.put("message", "Product not present in database");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        inventoryRepository.deleteByProductId(id);
        response.put("message", "Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable Integer quantity,
                                    @PathVariable Long storeId,
                                    @PathVariable Long productId) {
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
        return inventory != null && inventory.getStockLevel() >= quantity;
    }
}
