package com.shop.ecommerce.services;


import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.*;
import com.shop.ecommerce.models.enums.InteractionType;
import com.shop.ecommerce.models.enums.ProductStatus;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.repositories.UserProductInteractionRepository;
import com.shop.ecommerce.requests.ProductFilter;
import com.shop.ecommerce.requests.ProductFilterRequest;
import com.shop.ecommerce.requests.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.springframework.data.mongodb.core.query.Query;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserProductInteractionService userProductInteractionService;
    private final UserProductInteractionRepository userProductInteractionRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;


    public Product updateProductStatusToStored(String productId,ProductVariation productVariation) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getStatus() == ProductStatus.DRAFT) {
            product.setStatus(ProductStatus.STORED);
        }
        product.getProductVariations().add(productVariation);
        return productRepository.save(product);
    }

    @Override
    public Product addProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .categorySlug(request.getCategorySlug())
                .slug(request.getSlug())
                .status(ProductStatus.DRAFT)
                .productVariations(request.getProductVariations())
                .images(new ArrayList<>())
                .build();
        return productRepository.save(product);
    }



    @Override
    public List<Product> findProductsDraft() {
        return productRepository.findProductsByStatus(ProductStatus.DRAFT)
                .orElse(new ArrayList<>());
    }


    @Override
    public Product findProductById(String productId, String userId) {
        // Tìm sản phẩm trong cơ sở dữ liệu
        logger.info( "productId "+ productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (userId != null && product != null) {
            UserProductInteraction userProductInteraction = UserProductInteraction.builder()
                    .userId(userId)
                    .productId(productId)
                    .interactionType(InteractionType.VIEWED)
                    .interactionTime(LocalDateTime.now())
                    .build();

            userProductInteractionRepository.save(userProductInteraction);


        }

        return product;
    }



    @Override
    public List<Product> findProductsWithFilters(ProductFilterRequest request, String userId) {
        List<Product> products = List.of();
        logger.info("findProductsWithFilters request: " + request);
        if(request.isAllProduct()){
            products = findProductsByCriteria(new Criteria(), request);
        } else {
            if (request.getProductFilter() != null) {
                ProductFilter filter = request.getProductFilter();

                if (filter.getCategorySlug() != null && !filter.getCategorySlug().isEmpty()) {
                    Criteria criteria = new Criteria("status").is(ProductStatus.PUBLISHED);
                    products = findProductsByCriteria(criteria.and("category_slug").regex(filter.getCategorySlug()), request);
                }

                if (products.isEmpty() && filter.getName() != null && !filter.getName().isEmpty()) {
                    Criteria criteria = new Criteria("status").is(ProductStatus.PUBLISHED);
                    products = findProductsByCriteria(criteria.and("name").regex(filter.getName(), "i"), request);
                }
                // Ưu tiên 2: Tìm theo brand nếu không tìm thấy bằng name
                if (products.isEmpty() && filter.getBrand() != null && !filter.getBrand().isEmpty()) {
                    Criteria criteria = new Criteria("status").is(ProductStatus.PUBLISHED);
                    products = findProductsByCriteria(criteria.and("brand").regex(filter.getBrand()), request);
                }


                if (products.isEmpty()) {
                    products = findProductsByCriteria(new Criteria("status").is(ProductStatus.PUBLISHED), request);
                }

                if (products.isEmpty() && filter.getDiscountPercent() > 0) {
                    products = findProductsByCriteria(new Criteria("discount_percent").gte(filter.getDiscountPercent()), request);
                }
                if (products.isEmpty()) {
                    throw new NotFoundException("Products not found");
                }
            } else {
                products = findProductsByCriteria(new Criteria("status").is(ProductStatus.PUBLISHED), request);
            }
        }
        logger.info(" userId = " + userId);
        if (userId != null && !products.isEmpty() && products.get(0).getId() != null) {
            logger.info(" userId = " + userId);
            logger.info("productId = " + products.get(0).getId());
            logger.info("InteractionType = SEARCH");
            UserProductInteraction userProductInteraction = UserProductInteraction.builder()
                    .userId(userId)
                    .productId(products.get(0).getId())
                    .interactionType(InteractionType.SEARCH)
                    .interactionTime(LocalDateTime.now())
                    .build();

            userProductInteractionRepository.save(userProductInteraction);


        }
        return products;
    }


//    @Override
//    public List<Product> findProductsWithCategoryAndKeywordFilter(ProductFilterRequest request) {
//        List<Product> products = List.of();
//
//        if (request == null || request.getProductFilter() == null) {
//            return findProductsByCriteria(new Criteria(), request);
//        }
//
//        ProductFilter filter = request.getProductFilter();
//        Criteria criteria = new Criteria("status").is(ProductStatus.PUBLISHED);
//
//        if (filter.getCategorySlug() != null && !filter.getCategorySlug().isEmpty()) {
//            criteria.and("category_slug").is(filter.getCategorySlug());
//            products = findProductsByCriteria(criteria, request);
//        }
//
//        if (filter.getName() != null && !filter.getName().isEmpty()) {
//            criteria.and("name").regex(filter.getName(), "i");
//            products = findProductsByCriteria(criteria, request);
//        }
//        if (products.isEmpty() && filter.getBrand() != null && !filter.getBrand().isEmpty()) {
//            criteria.and("name").regex(filter.getName(), "i");
//            products = findProductsByCriteria(criteria, request);
//        }
//
//        if (products.isEmpty()) {
//            throw new NotFoundException("Products not found");
//        }
//        return products;
//    }

    @Override
    public List<Product> findProductsWithCategoryAndKeywordFilter(ProductFilterRequest request) {
        // Trả về danh sách rỗng nếu request hoặc filter không hợp lệ
        if (request == null || request.getProductFilter() == null) {
            return findProductsByCriteria(new Criteria(), request);
        }

        ProductFilter filter = request.getProductFilter();
        Criteria criteria = new Criteria("status").is(ProductStatus.PUBLISHED);

        // Áp dụng filter category_slug
        if (filter.getCategorySlug() != null && !filter.getCategorySlug().isEmpty()) {
            criteria.and("category_slug").is(filter.getCategorySlug());
            List<Product> productsByCategory = findProductsByCriteria(criteria, request);

            // Nếu không có sản phẩm nào khớp category_slug, trả về danh sách rỗng
            if (productsByCategory.isEmpty()) {
                return List.of();
            }
        }

        // Áp dụng filter name
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            criteria.and("name").regex(filter.getName(), "i");
        }

        // Áp dụng filter brand
        if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
            criteria.and("brand").is(filter.getBrand());
        }

        // Truy vấn dựa trên các tiêu chí còn lại
        List<Product> products = findProductsByCriteria(criteria, request);

        // Nếu không tìm thấy sản phẩm nào, ném lỗi NotFoundException
        if (products.isEmpty()) {
            throw new NotFoundException("Products not found");
        }
        return products;
    }




    private List<Product> findProductsByCriteria(Criteria criteria, ProductFilterRequest request) {
        int skip = (request.getPage() - 1) * request.getLimit();

        String sortField = request.getSort() != null ? request.getSort() : "_id";
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(request.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getLimit(),
                Sort.by(sortDirection, sortField)
        );

        Query query = new Query(criteria).with(pageable);


        if (request.getSelectFields() != null && !request.getSelectFields().isEmpty()) {
            for (String field : request.getSelectFields()) {
                query.fields().include(field);
            }
        }
        if (request.getUnSelectFields() != null && !request.getUnSelectFields().isEmpty()) {
            for (String field : request.getUnSelectFields()) {
                query.fields().exclude(field);
            }
        }

        // Đếm tổng số sản phẩm phù hợp
        long totalProducts = mongoTemplate.count(new Query(criteria), Product.class);

        // Kiểm tra nếu skip vượt quá tổng số lượng sản phẩm
        if (skip >= totalProducts) {
            return List.of(); // Trả về danh sách rỗng nếu không còn sản phẩm để phân trang
        }

        return mongoTemplate.find(query.skip(skip), Product.class);
    }


    @Override
    public Product searchProductByUser(String keySearch) {
        return null;
    }


    @Override
    public List<Product> getTopSellingProducts(int limit) {
        return productRepository.findByOrderByTotalSoldDesc(PageRequest.of(0, limit));
    }

}
