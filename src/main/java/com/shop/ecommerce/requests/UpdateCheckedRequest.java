package com.shop.ecommerce.requests;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCheckedRequest {
    private String cartId; // ID của người dùng
    private List<String> cartProductIds; // Danh sách ID sản phẩm
}