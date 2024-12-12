package com.shop.ecommerce.Utils;

import java.util.Random;

public class ProductVariationUtils {

    // Phương thức để tạo ID tùy chỉnh
    public static String generateProductSku(String productId) {
        String randomSixDigit = generateRandomSixDigit();
        return productId + "." + randomSixDigit;
    }

    // Phương thức để sinh ra chuỗi ngẫu nhiên gồm 6 chữ số
    public static String generateRandomSixDigit() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // Sinh ra số từ 100000 đến 999999
        return String.valueOf(number);
    }
}
