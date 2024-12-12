package com.shop.ecommerce.Utils;
import com.shop.ecommerce.models.UserProductInteraction;

import java.util.*;
import java.util.stream.Collectors;

public class InteractionVectorBuilder {

    public static int[] buildUserInteractionVector(String userId, List<String> allProductIds, List<UserProductInteraction> interactions) {
        int[] vector = new int[allProductIds.size()];

        for (UserProductInteraction interaction : interactions) {
            String productId = interaction.getProductId();
            int productIndex = allProductIds.indexOf(productId);
            if (productIndex != -1) {
                vector[productIndex] += interaction.getInteractionType().getWeight();
            }
        }
        return vector;
    }

//    public static int[] buildUserInteractionVector(String userId, List<String> allProductIds,
//                                                   List<UserProductInteraction> userInteractions) {
//        int[] vector = new int[allProductIds.size()];
//
//        // Lấy danh sách ID sản phẩm mà người dùng đã tương tác
//        Set<String> interactedProducts = userInteractions.stream()
//                .filter(interaction -> interaction.getUserId().equals(userId))
//                .map(UserProductInteraction::getProductId)
//                .collect(Collectors.toSet());
//
//        // Tạo vector
//        for (int i = 0; i < allProductIds.size(); i++) {
//            String productId = allProductIds.get(i);
//            vector[i] = interactedProducts.contains(productId) ? 1 : 0;
//        }
//
//        return vector;
//    }

}
