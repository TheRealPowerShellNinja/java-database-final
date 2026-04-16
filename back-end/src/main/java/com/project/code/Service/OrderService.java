package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO request) {
        // 1. Znajdź lub utwórz klienta
        Customer customer = customerRepository.findByEmail(request.getCustomerEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setName(request.getCustomerName());
            customer.setEmail(request.getCustomerEmail());
            customer.setPhone(request.getCustomerPhone());
            customer = customerRepository.save(customer);
        }

        // 2. Znajdź sklep
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // 3. Utwórz OrderDetails i zapisz (3 punkty)
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(request.getTotalPrice());
        orderDetails.setOrderDate(LocalDateTime.now());

        // Zapisujemy i używamy zwróconego obiektu
        OrderDetails savedOrder = orderDetailsRepository.save(orderDetails);

        // 4. Dla każdego produktu: zmniejsz stan magazynowy i zapisz (3 punkty)
        for (PurchaseProductDTO productDTO : request.getPurchaseProduct()) {
            Inventory inventory = inventoryRepository.findByProductIdandStoreId(
                    productDTO.getId(), request.getStoreId());
            if (inventory == null) {
                throw new RuntimeException("Inventory not found for product " + productDTO.getId());
            }
            // Zmniejsz stan
            int newStock = inventory.getStockLevel() - productDTO.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product " + productDTO.getId());
            }
            inventory.setStockLevel(newStock);
            inventoryRepository.save(inventory);   // zapis zaktualizowanego inventory

            // Utwórz OrderItem
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(productRepository.findByid(productDTO.getId()));
            item.setQuantity(productDTO.getQuantity());
            item.setPrice(productDTO.getPrice() * productDTO.getQuantity());
            orderItemRepository.save(item);
        }
    }
}
