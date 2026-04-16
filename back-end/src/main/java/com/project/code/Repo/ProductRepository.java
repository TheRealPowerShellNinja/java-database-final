package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    Product findById(long id);

    // 1. Filter products by category
    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> findByCategory(String category);

    // 2. Filter products by storeId and category
    @Query("SELECT p FROM Product p JOIN Inventory i ON p.id = i.product.id " +
           "WHERE i.store.id = :storeId AND p.category = :category")
    List<Product> findByStoreIdAndCategory(Long storeId, String category);
}
