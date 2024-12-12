package com.shop.ecommerce.Utils;

import com.shop.ecommerce.models.UserProductInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class RecommendationSystem {
//
//    @Autowired
//    private UserProductInteractionService interactionService;
//
//    public void calculateSimilarity() {
//        // Danh sách tất cả các sản phẩm (có thể lấy từ database hoặc cài đặt sẵn)
//        List<String> allProductIds = Arrays.asList("product1", "product2", "product3");
//
//        // Lấy dữ liệu tương tác của từng người dùng
//        List<UserProductInteraction> userAData = interactionService.getUserInteractions("userA");
//        List<UserProductInteraction> userBData = interactionService.getUserInteractions("userB");
//
//        // Tạo vector tương tác cho userA và userB
//        int[] userAInteractions = InteractionVectorBuilder.buildUserInteractionVector("userA", allProductIds, userAData);
//        int[] userBInteractions = InteractionVectorBuilder.buildUserInteractionVector("userB", allProductIds, userBData);
//
//        // Tính toán độ tương tự
//        double similarity = SimilarityUtils.cosineSimilarity(userAInteractions, userBInteractions);
//        System.out.println("Độ tương tự giữa userA và userB: " + similarity);
//    }
//}
