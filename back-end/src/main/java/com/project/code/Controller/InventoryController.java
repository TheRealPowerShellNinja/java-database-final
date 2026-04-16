package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    // 🔴 KEY: GET /filter/{category}/{name}/{storeid}
    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(@PathVariable String category,
                                              @PathVariable String name,
                                              @PathVariable long storeid) {

        Map<String, Object> map = new HashMap<>();

        if (category.equals("null")) {
            map.put("product", productRepository.findByNameLike(storeid, name));
            return map;
        } else if (name.equals("null")) {
            map.put("product", productRepository.findByCategoryAndStoreId(storeid, category));
            return map;
        }

        map.put("product", productRepository.findByNameAndCategory(storeid, name, category));
        return map;
    }

    // 🔴 KEY: GET /validate/{quantity}/{storeId}/{productId}
    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable int quantity,
                                    @PathVariable long storeId,
                                    @PathVariable long productId) {

        Inventory result = inventoryRepository.findByProductIdandStoreId(productId, storeId);

        if (result != null && result.getStockLevel() >= quantity) {
            return true;
        }
        return false;
    }

    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {

        Product product = request.getProduct();
        Inventory inventory = request.getInventory();

        Map<String, String> map = new HashMap<>();

        if (!serviceClass.ValidateProductId(product.getId())) {
            map.put("message", "Id " + product.getId() + " not present in database");
            return map;
        }

        productRepository.save(product);
        map.put("message", "Successfully updated product with id: " + product.getId());

        if (inventory != null) {
            try {
                Inventory result = serviceClass.getInventoryId(inventory);

                if (result != null) {
                    inventory.setId(result.getId());
                    inventoryRepository.save(inventory);
                } else {
                    map.put("message", "No data available for this product or store id");
                    return map;
                }

            } catch (DataIntegrityViolationException e) {
                map.put("message", "Error: " + e);
                return map;
            } catch (Exception e) {
                map.put("message", "Error: " + e);
                return map;
            }
        }

        return map;
    }

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {

        Map<String, String> map = new HashMap<>();

        try {
            if (serviceClass.validateInventory(inventory)) {
                inventoryRepository.save(inventory);
            } else {
                map.put("message", "Data Already present in inventory");
                return map;
            }

        } catch (DataIntegrityViolationException e) {
            map.put("message", "Error: " + e);
            return map;
        } catch (Exception e) {
            map.put("message", "Error: " + e);
            return map;
        }

        map.put("message", "Product added to inventory successfully");
        return map;
    }

    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeid) {

        Map<String, Object> map = new HashMap<>();
        List result = productRepository.findProductsByStoreId(storeid);
        map.put("products", result);
        return map;
    }

    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name,
                                             @PathVariable long storeId) {

        Map<String, Object> map = new HashMap<>();
        map.put("product", productRepository.findByNameLike(storeId, name));
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {

        Map<String, String> map = new HashMap<>();

        if (!serviceClass.ValidateProductId(id)) {
            map.put("message", "Id " + id + " not present in database");
            return map;
        }

        inventoryRepository.deleteByProductId(id);
        map.put("message", "Deleted product successfully with id: " + id);
        return map;
    }
}
