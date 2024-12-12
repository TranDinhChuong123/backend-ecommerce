package com.shop.ecommerce.services;
import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.SKUDetails;
import com.shop.ecommerce.models.enums.ProductAttributeStatus;
import com.shop.ecommerce.models.enums.ProductAttributeStatus;
import com.shop.ecommerce.responses.ProductDetailResponse;
import com.shop.ecommerce.responses.SKUDetailsResponse;

import java.util.List;
import java.util.Optional;

public interface SKUDetailsService {

    public List<SKUDetailsResponse> findByStatus(ProductAttributeStatus status);
    List<SKUDetailsResponse> getAllSKUDetails();
    SKUDetails createSKUDetails(SKUDetails SKUDetails);
    SKUDetails updateSKUDetails(String id, SKUDetails SKUDetails);

    SKUDetails updateStatus(String variationId, ProductAttributeStatus status);


    ProductDetailResponse getProductStoredVariationId(String variantId);

    SKUDetails updateSKUWithStoredStatus(SKUDetails updateStatus);
    boolean isPublishedProduct(String variantId);
}
