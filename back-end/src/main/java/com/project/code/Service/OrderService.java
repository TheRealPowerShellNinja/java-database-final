package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public void saveOrder(PlaceOrderRequestDTO request) {

        // 1. Get or Create Customer
        Customer customer = customerRepository.findByEmail(request.getCustomerEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setName(request.getCustomerName());
            customer.setEmail(request.getCustomerEmail());
            customer.setMobileNo(request.getCustomerPhone());
            customer = customerRepository.save(customer);
        }

        // 2. Get Store
        Store store = storeRepository.findById(request.getStoreId()).orElse(null);
        if (store == null) {
            throw new RuntimeException("Store not found");
        }

        // 3. Create OrderDetails
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(request.getTotalPrice());
        orderDetails.setDate(LocalDateTime.now());

        orderDetails = orderDetailsRepository.save(orderDetails);

        // 4. Process Order Items
        for (PurchaseProductDTO item : request.getPurchaseProduct()) {

            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(
                    item.getId(),
                    request.getStoreId()
            );

            if (inventory != null) {
                // Update stock
                inventory.setStockLevel(inventory.getStockLevel() - item.getQuantity());
                inventoryRepository.save(inventory);

                // Save OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orderDetails);
                orderItem.setProduct(productRepository.findById(item.getId()).orElse(null));
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());

                orderItemRepository.save(orderItem);
            }
        }
    }
}