package com.shop.ecommerce.models.enums;

import lombok.Getter;

@Getter
public enum CartState {
    ACTIVE("active"),
    COMPLETED("completed"),
    FAILED("failed"),
    PENDING("pending");

    private final String value;

    CartState(String value) {
        this.value = value;
    }

    public static CartState fromValue(String value) {
        for (CartState state : values()) {
            if (state.getValue().equals(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid cart state value: " + value);
    }
}
