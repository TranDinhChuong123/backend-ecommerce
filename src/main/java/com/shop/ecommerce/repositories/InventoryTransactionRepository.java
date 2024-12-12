package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.InventoryTransaction;
import com.shop.ecommerce.models.enums.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface InventoryTransactionRepository extends MongoRepository<InventoryTransaction, String> {
    List<InventoryTransaction> findByType(TransactionType type);
    List<InventoryTransaction> findByInventoryId(String inventoryId);
    List<InventoryTransaction> findByInventoryIdAndType(String inventoryId, TransactionType type);
    List<InventoryTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}