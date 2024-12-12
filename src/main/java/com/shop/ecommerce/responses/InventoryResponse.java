package com.shop.ecommerce.responses;

import com.shop.ecommerce.models.ProductVariation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    @Id
    private String id;
    private ObjectId productId;
    private String productName;
    private String variationId;

    private String urlImage;
    private String color;
    private String capacity;
    private String size;

    private int totalQuantityIn;

    private int quantityInStock;

    private LocalDateTime date;
    private String supplierId;

}
