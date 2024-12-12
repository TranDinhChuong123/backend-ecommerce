package com.shop.ecommerce.services;


import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.Inventory;
import com.shop.ecommerce.models.InventoryTransaction;
import com.shop.ecommerce.models.enums.TransactionType;
import com.shop.ecommerce.repositories.InventoryRepository;
import com.shop.ecommerce.repositories.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryRepository inventoryRepository;
    @Override
    public InventoryTransaction createTransaction(InventoryTransaction transaction) {
        transaction.setTransactionDate(java.time.LocalDateTime.now());

        Inventory inventory = inventoryRepository.findById(transaction.getInventoryId())
                .orElseThrow(()-> new NotFoundException("Not Found inventory"));

        if(transaction.getType() == TransactionType.IN){
            inventory.setTotalQuantityIn(inventory.getTotalQuantityIn() + transaction.getQuantity());
            inventory.setQuantityInStock(inventory.getQuantityInStock() + transaction.getQuantity());
            inventory.setTotalCost(inventory.getTotalCost() + transaction.getTotalPrice());
            inventoryRepository.save(inventory);
        }
        return transactionRepository.save(transaction);

    }

    @Override
    public List<InventoryTransaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type);
    }


    @Override
    public List<InventoryTransaction> getTransactionsByInventoryId(String inventoryId) {
        return transactionRepository.findByInventoryId(inventoryId);
    }

    @Override
    public List<InventoryTransaction> getTransactionsByInventoryIdAndType(String inventoryId, TransactionType type) {
        return transactionRepository.findByInventoryIdAndType(inventoryId, type);
    }


}
