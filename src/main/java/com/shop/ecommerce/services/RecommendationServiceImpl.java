package com.shop.ecommerce.services;

import com.shop.ecommerce.Utils.InteractionVectorBuilder;
import com.shop.ecommerce.Utils.SimilarityUtils;
import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.UserProductInteraction;
import com.shop.ecommerce.models.enums.ProductStatus;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.repositories.UserProductInteractionRepository;
import com.shop.ecommerce.requests.ProductFilter;
import com.shop.ecommerce.requests.ProductFilterRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService{

    private final ProductRepository productRepository;
    private final UserProductInteractionRepository userInteractionRepository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final ProductService productService;
    // Phương thức chính để lấy gợi ý sản phẩm cho người dùng
    public List<String> recommendProducts(String userId, List<String> allProductIds, List<UserProductInteraction> allInteractions) {

        // Bước 1: Tạo vector tương tác cho người dùng hiện tại
        int[] userVector = InteractionVectorBuilder.buildUserInteractionVector(userId, allProductIds, allInteractions);

        // Bước 2: Tìm các user có độ tương đồng cao nhất với user hiện tại
        Set<String> otherUsers = allInteractions.stream()
                .filter(interaction -> interaction.getProductId() != null && interaction.getUserId() != null)
                .map(UserProductInteraction::getUserId)
                .collect(Collectors.toSet());




        Map<String, Double> userSimilarityScores = new HashMap<>();
        for (String otherUserId : otherUsers) {
            int[] otherUserVector = InteractionVectorBuilder.buildUserInteractionVector(otherUserId, allProductIds, allInteractions);
            double similarity = SimilarityUtils.cosineSimilarity(userVector, otherUserVector);
            userSimilarityScores.put(otherUserId, similarity);
        }

        // Bước 3: Lọc những người dùng có độ tương đồng cao nhất
        List<String> similarUsers = userSimilarityScores.entrySet().stream()
//                .filter(entry -> entry.getValue() > 0.5) // lọc độ tương đồng trên 0.5 (có thể điều chỉnh)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(5)
                .toList();

        // Bước 4: Lấy các sản phẩm mà người dùng hiện tại
        List<UserProductInteraction> userInteractions = allInteractions.stream()
                .filter(interaction -> interaction.getUserId().equals(userId))
                .sorted(Comparator.comparing(UserProductInteraction::getInteractionTime).reversed()) // Sắp xếp theo thời gian giảm dần
                .toList();




        // Bước 2: Tạo danh sách sản phẩm gợi ý, ưu tiên theo thời gian gần nhất
        Map<String, Integer> recommendedProducts = new LinkedHashMap<>();

        for (UserProductInteraction interaction : userInteractions) {
            String productId = interaction.getProductId();
            recommendedProducts.put(productId, recommendedProducts.getOrDefault(productId, 0) + 1); // Cộng trọng số (ở đây là 1)
        }


        for (String similarUserId : similarUsers) {
            List<UserProductInteraction> similarUserInteractions = allInteractions.stream()
                    .filter(interaction -> interaction.getUserId().equals(similarUserId))
                    .sorted(Comparator.comparing(UserProductInteraction::getInteractionTime).reversed())
                    .toList();

            for (UserProductInteraction interaction : similarUserInteractions) {
                String productId = interaction.getProductId();
                recommendedProducts.put(productId, recommendedProducts.getOrDefault(productId, 0) + interaction.getInteractionType().getWeight());
            }
        }

        return recommendedProducts.entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(30)
                .collect(Collectors.toList());
    }

    public List<Product> getRecommendationsForUser(String userId) {
        List<Product> allProducts = productRepository.findAll();
        List<String> allProductIds = allProducts.stream().map(Product::getId).toList();

        List<UserProductInteraction> allInteractions =
                userInteractionRepository.findAll().stream()
                        .filter(userProductInteraction->
                                userProductInteraction.getUserId() != null
                                        && userProductInteraction.getProductId() != null).toList();

        List<String> recommendedProductIds = recommendProducts(userId, allProductIds, allInteractions);
        logger.info("Recommended product IDs "+ recommendedProductIds);

        List<Product> products = productRepository.findAllById(recommendedProductIds);
        Map<String, Product> productMap = products.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<Product> sortedProducts = recommendedProductIds.stream()
                .map(productMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!sortedProducts.isEmpty()) {
            Product firstProduct = sortedProducts.get(0);
            String categorySlug = firstProduct.getCategorySlug();


            ProductFilterRequest request = new ProductFilterRequest();
            request.setProductFilter(new ProductFilter());
            request.getProductFilter().setCategorySlug(categorySlug);

            List<Product> sameCategoryProducts = productService.findProductsWithCategoryAndKeywordFilter(request);

            List<Product> filteredSameCategoryProducts = sameCategoryProducts.stream()
                    .filter(product -> !product.getId().equals(firstProduct.getId()))
                    .limit(9)
                    .toList();
            sortedProducts.addAll(1, filteredSameCategoryProducts);
        }
        return sortedProducts;

    }


}
