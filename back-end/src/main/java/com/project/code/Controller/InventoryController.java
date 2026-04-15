package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> response = new HashMap<>();
    
        boolean valid = serviceClass.validateInventory(inventory);
        if (valid) {
            inventoryRepository.save(inventory);
            response.put("message", "Inventory added successfully");
        } else {
            response.put("message", "Inventory already exists");
        }
    
        return response;
    }
    
    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> response = new HashMap<>();
    
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(
            request.getProduct().getId(),
            request.getInventory().getStore().getId()
        );
    
        if (inventory != null) {
            inventory.setStockLevel(request.getInventory().getStockLevel());
            inventoryRepository.save(inventory);
            response.put("message", "Inventory updated successfully");
        } else {
            response.put("message", "Inventory not found");
        }
    
        return response;
    }
    
    @GetMapping("/products/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable long storeId) {
        Map<String, Object> response = new HashMap<>();
    
        response.put("products", inventoryRepository.findByStoreId(storeId));
    
        return response;
    }
    
    @GetMapping("/validate")
    public boolean validateQuantity(@RequestParam long productId,
                                    @RequestParam long storeId,
                                    @RequestParam int quantity) {
    
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
    
        return inventory != null && inventory.getStockLevel() >= quantity;
    }
}