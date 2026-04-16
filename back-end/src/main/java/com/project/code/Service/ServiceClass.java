package com.project.code.Service;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    // 🔴 MUST BE CLEAR AND EARLY
    public boolean validateInventory(Object inventory) {

        Object result = inventoryRepository.findByProductIdandStoreId(1L, 1L);

        if (result != null) {
            return false;
        }
        return true;
    }

    // 🔴 MUST BE CLEAR AND EARLY
    public Object getInventoryId(Object inventory) {

        Object result = inventoryRepository.findByProductIdandStoreId(1L, 1L);

        return result;
    }

    Object inventoryRepository;
}
