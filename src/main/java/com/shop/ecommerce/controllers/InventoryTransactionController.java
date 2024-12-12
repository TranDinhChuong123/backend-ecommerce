package com.shop.ecommerce.controllers;


import com.shop.ecommerce.models.InventoryTransaction;
import com.shop.ecommerce.models.enums.TransactionType;
import com.shop.ecommerce.requests.OrderRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory-transactions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventoryTransactionController {


    private final InventoryTransactionService transactionService;
    @PostMapping("/create")
    public ApiResponse<InventoryTransaction> createNewOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody InventoryTransaction request
    ) {
        request.setPerformedBy(userDetails.getUsername());
        request.setTransactionDate(LocalDateTime.now());
        return new ApiResponse<>(
                200,
                "Order created successfully",
                transactionService.createTransaction(request)
        );
    }


    @GetMapping("/type/{type}")
    public ApiResponse<List<InventoryTransaction>> getTransactionsByType(@PathVariable TransactionType type) {
        List<InventoryTransaction> transactions = transactionService.getTransactionsByType(type);
        return  new ApiResponse<>(
                200,
                "getTransactionsByType successfully",
                transactions
        );
    }




    // Thêm endpoint tìm theo InventoryId và Type
    @GetMapping("/inventory/{inventoryId}/type/{type}")
    public ApiResponse<List<InventoryTransaction>> getTransactionsByInventoryIdAndType(
            @PathVariable String inventoryId,
            @PathVariable TransactionType type) {
        return  new ApiResponse<>(
                200,
                "getTransactionsByType successfully",
                transactionService.getTransactionsByInventoryIdAndType(inventoryId, type)
        );
    }
}

