package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public boolean validateInventory(Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );

        if (existingInventory != null) {
            return false;
        }

        return true;
    }

    public boolean validateProduct(Product product) {
        Product existingProduct = productRepository.findByName(product.getName());

        if (existingProduct != null) {
            return false;
        }

        return true;
    }

    public boolean validateProductId(long id) {
        Product product = productRepository.findById(id);

        if (product == null) {
            return false;
        }

        return true;
    }

    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }
}
