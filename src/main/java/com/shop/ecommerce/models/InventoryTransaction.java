package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "inventory_transactions")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    @Id
    private String id;
    private String inventoryId;
    private TransactionType type;
    private int quantity;             // Số lượng giao dịch
    private Double totalPrice;        // Tổng giá trị giao dịch (quantity * price)
    private LocalDateTime transactionDate; // Thời gian giao dịch
    private String orderId; //
    private String performedBy;       // Người thực hiện giao dịch
    private String supplierId;
}
