package com.shop.ecommerce.models.enums;

import lombok.Getter;

@Getter
public enum InteractionType {
    VIEWED(1),       // Xem sản phẩm
    ADDED_TO_CART(2),// Thêm vào giỏ hàng
    PURCHASED(3),   // Đã mua
    SEARCH(1);

    private final int weight;

    InteractionType(int weight) {
        this.weight = weight;
    }

}
