package com.shop.ecommerce.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "inventories")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    private String id;
    private ObjectId productId;
    private String variationId;
    private int quantityInStock;
    private int totalQuantityIn;
    private Double totalCost;

    @Field(name = "warehouse_name")
    private String warehouseName;

    @Field(name = "warehouse_address")
    private String warehouseAddress;

    @Field(name = "manager_contact")
    private String managerContact;

    @Field(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

}
