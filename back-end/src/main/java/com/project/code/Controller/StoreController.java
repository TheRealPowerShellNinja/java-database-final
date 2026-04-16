package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {
    
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public ResponseEntity<Map<String, String>> addStore(@RequestBody Store store) {
        Map<String, String> response = new HashMap<>();
        try {
            storeRepository.save(store);
            response.put("message", "Store added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error adding store: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/validate/{storeId}")
    public ResponseEntity<Boolean> validateStore(@PathVariable Long storeId) {
        boolean exists = storeRepository.findById(storeId).isPresent();
        return ResponseEntity.ok(exists);
    }
    
    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.saveOrder(placeOrderRequest);
            response.put("message", "Order placed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error placing order: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
