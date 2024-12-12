package com.shop.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    private String Id;                    // ID đơn hàng
    private String name;                 // Tên sản phẩm tại thời điểm đặt hàng
    private String productId;            // ID của sản phẩm gốc
    private String selectedVariationId;  // ID của biến thể sản phẩm (nếu có)
    private String urlImage;             // URL ảnh của sản phẩm (nếu có)
    private String color;                // Màu sắc của sản phẩm (nếu có)
    private String size;                 // Kích thước của sản phẩm (nếu có)
    private int price;
    private int discountPercent;         // Phần trăm giảm giá (nếu có)
    private int buyQuantity;             // Số lượng sản phẩm mua

}
