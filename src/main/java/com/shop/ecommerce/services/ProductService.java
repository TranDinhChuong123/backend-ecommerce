package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.ProductVariation;
import com.shop.ecommerce.requests.ProductFilterRequest;
import com.shop.ecommerce.requests.ProductRequest;
import java.util.List;


public interface ProductService {
    List<Product> getTopSellingProducts(int limit);
    Product addProduct(ProductRequest productRequest);
    Product updateProductStatusToStored(String productId, ProductVariation variation);
    List<Product> findProductsDraft();
    Product findProductById(String productId, String userId);
    Product searchProductByUser(String keySearch);

    List<Product> findProductsWithFilters(ProductFilterRequest request, String userId);
    List<Product> findProductsWithCategoryAndKeywordFilter(ProductFilterRequest request);




}
