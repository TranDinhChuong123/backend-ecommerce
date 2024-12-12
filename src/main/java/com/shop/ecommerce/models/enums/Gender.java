package com.shop.ecommerce.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    @JsonCreator
    public static Gender fromString(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return null; // Trả về null nếu giá trị là chuỗi rỗng hoặc null
        }
        return Gender.valueOf(gender.toUpperCase());
    }
}
