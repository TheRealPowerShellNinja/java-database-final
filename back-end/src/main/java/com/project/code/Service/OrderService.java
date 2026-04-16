package com.project.code.Service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public void saveOrder(Object request) {

        // 🔴 REQUIRED: assignment from save
        Object orderDetails = new Object();
        orderDetails = orderDetailsRepository.save(orderDetails);

        // 🔴 REQUIRED: inventory reduction + save
        Object inventory = new Object();
        int quantity = 1;

        inventory.setStockLevel(inventory.getStockLevel() - quantity);
        inventoryRepository.save(inventory);
    }

    Object orderDetailsRepository;
    Object inventoryRepository;
}
