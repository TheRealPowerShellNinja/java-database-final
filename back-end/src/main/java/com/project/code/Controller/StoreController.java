package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    // 🔴 MOVE THIS TO TOP (CRITICAL)
    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        Store store = storeRepository.findByid(storeId);
        return store != null;
    }

    // 🔴 MOVE THIS UP (CRITICAL)
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequest) {

        Map<String, String> map = new HashMap<>();

        try {
            orderService.saveOrder(placeOrderRequest);
            map.put("message", "Order placed successfully");
        } catch (Exception e) {
            map.put("error", e.toString());
        }

        return map;
    }
}
