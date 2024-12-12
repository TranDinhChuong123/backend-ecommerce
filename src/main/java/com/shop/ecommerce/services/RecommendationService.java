package com.shop.ecommerce.services;
import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.UserProductInteraction;
import java.util.List;

public interface RecommendationService {
    public List<String> recommendProducts(
            String userId,
            List<String> allProductIds,
            List<UserProductInteraction> allInteractions
    );
    public List<Product> getRecommendationsForUser(String userId);

}
