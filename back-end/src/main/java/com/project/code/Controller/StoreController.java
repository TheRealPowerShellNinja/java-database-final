package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> response = new HashMap<>();
        storeRepository.save(store);
        response.put("message", "Store added successfully");
        return response;
    }

    @GetMapping("/validate/{storeId}")
    public boolean validateStore(@PathVariable long storeId) {
        Store store = storeRepository.findById(storeId);
        return store != null;
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO request) {
        Map<String, String> response = new HashMap<>();
    
        try {
            orderService.saveOrder(request);
            response.put("message", "Order placed successfully");
        } catch (Exception e) {
            response.put("Error", "Error processing order");
        }
    
        return response;
    }
}