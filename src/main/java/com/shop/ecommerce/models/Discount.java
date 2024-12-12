package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.AppliesTo;
import com.shop.ecommerce.models.enums.DiscountType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "discounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    private String id;

    @Field(name = "discount_name")
    private String discountName;

    @Field(name = "discount_description")
    private String discountDescription;

    @Field(name = "discount_type")
    private DiscountType discountType; // Enum to represent discount type

    @Field(name = "discount_value")
    private BigDecimal discountValue;

    @Field(name = "discount_max_value")
    private BigDecimal discountMaxValue;

    @Field(name = "discount_code")
    private String discountCode;

    @Field(name = "discount_start_date")
    private LocalDate discountStartDate;

    @Field(name = "discount_end_date")
    private LocalDate discountEndDate;

    @Field(name = "discount_max_uses")
    private Integer discountMaxUses;

    @Field(name = "discount_uses_count")
    private Integer discountUsesCount;

    @Field(name = "discount_user_used")
    private List<String> discountUserUsed; // List of user IDs who have used the discount

    @Field(name = "discount_max_uses_per_user") // so luong cho phep toi da su dung
    private Integer discountMaxUsesPerUser;

    @Field(name = "discount_min_order_value")
    private BigDecimal discountMinOrderValue;


    @Field(name = "discount_is_active")
    private Boolean discountIsActive;

    @Field(name = "discount_applies_to")
    private AppliesTo discountAppliesTo; // Enum to represent discount applies to

    @Field(name = "discount_product_ids")
    private List<String> discountProductIds; // List of product IDs the discount applies to
}
