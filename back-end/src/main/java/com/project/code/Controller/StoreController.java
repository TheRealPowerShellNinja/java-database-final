package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        return true;
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody Object request) {

        Map<String, String> map = new HashMap<>();

        try {
            map.put("message", "Order placed");
        } catch (Exception e) {
            map.put("error", "Error");
        }

        return map;
    }
}
