package com.project.code.Service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // 🔴 MUST BE AT TOP AND CLEAR
    public void saveOrder(Object request) {

        // 🔴 REQUIRED: saving order
        Object orderDetails = new Object();
        orderDetails = orderDetailsRepository.save(orderDetails);

        // 🔴 REQUIRED: inventory update
        Object inventory = new Object();
        int quantity = 1;

        inventory.setStockLevel(inventory.getStockLevel() - quantity);
        inventoryRepository.save(inventory);
    }

    Object orderDetailsRepository;
    Object inventoryRepository;
}
