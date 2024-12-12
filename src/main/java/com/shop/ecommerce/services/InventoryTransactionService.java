package com.shop.ecommerce.services;

import com.shop.ecommerce.models.InventoryTransaction;
import com.shop.ecommerce.models.enums.TransactionType;

import java.util.List;

public interface InventoryTransactionService {
    List<InventoryTransaction> getTransactionsByInventoryId(String inventoryId);
    InventoryTransaction createTransaction(InventoryTransaction transaction);
    List<InventoryTransaction> getTransactionsByType(TransactionType type);

    List<InventoryTransaction> getTransactionsByInventoryIdAndType(String inventoryId, TransactionType type);
}
