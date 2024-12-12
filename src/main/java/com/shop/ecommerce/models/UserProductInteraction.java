package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.InteractionType;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_productInteractions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserProductInteraction {

    @Id
    private String id;
    private String userId;
    private String productId;
    private InteractionType interactionType;
    private LocalDateTime interactionTime;
}