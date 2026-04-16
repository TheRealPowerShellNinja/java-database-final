package com.project.code.Service;

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

        Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());

        if (customer == null) {
            customer = new Customer();
            customer.setName(placeOrderRequest.getCustomerName());
            customer.setEmail(placeOrderRequest.getCustomerEmail());
            customer.setMobileNo(placeOrderRequest.getCustomerPhone());
            customer = customerRepository.save(customer);
        }

        Store store = storeRepository.findById(placeOrderRequest.getStoreId());
        if (store == null) {
            throw new RuntimeException("Store not found");
        }

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
        orderDetails.setDate(LocalDateTime.now());

        OrderDetails savedOrderDetails = orderDetailsRepository.save(orderDetails);

        for (PurchaseProductDTO item : placeOrderRequest.getPurchaseProduct()) {

            Product product = productRepository.findById(item.getId());
            if (product == null) {
                continue;
            }

            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(item.getId(), placeOrderRequest.getStoreId());

            if (inventory != null) {
                inventory.setStockLevel(inventory.getStockLevel() - item.getQuantity());
                inventoryRepository.save(inventory);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrderDetails);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());

            orderItemRepository.save(orderItem);
        }
    }
}
