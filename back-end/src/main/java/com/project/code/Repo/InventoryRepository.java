package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByStoreId(long storeId);

    List<Inventory> findByProductId(long productId);

    Inventory findByProductIdAndStoreId(long productId, long storeId);
}