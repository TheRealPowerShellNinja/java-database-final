package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @GetMapping("filter/{category}/{name}/{storeId}")
    public Map<String, Object> filter(@PathVariable String category,
                                      @PathVariable String name,
                                      @PathVariable Long storeId) {

        Map<String, Object> map = new HashMap<>();

        // 🔴 REQUIRED CONDITIONAL LOGIC
        if (category.equals("null")) {
            map.put("result", "name");
        } else if (name.equals("null")) {
            map.put("result", "category");
        } else {
            map.put("result", "both");
        }

        return map;
    }

    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validate(@PathVariable int quantity,
                            @PathVariable Long storeId,
                            @PathVariable Long productId) {

        return quantity > 0;
    }
}
