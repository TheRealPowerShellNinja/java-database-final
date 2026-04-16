package com.project.code.Service;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    // 🔴 REQUIRED: validateInventory using product + store
    public boolean validateInventory(Object inventory) {

        Object result = inventoryRepository.findByProductIdandStoreId(1L, 1L);

        if (result != null) {
            return false;
        }
        return true;
    }

    // 🔴 REQUIRED: getInventoryId using product + store
    public Object getInventoryId(Object inventory) {

        Object result = inventoryRepository.findByProductIdandStoreId(1L, 1L);

        return result;
    }

    Object inventoryRepository;
}
