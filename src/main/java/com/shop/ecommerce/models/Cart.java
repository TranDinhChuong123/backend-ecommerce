package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.CartState;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Document(collection = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    private String id;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "cart_products")
    private List<CartProduct> cartProducts;

    @Field(name = "cart_state")
    private CartState cartState;

}
