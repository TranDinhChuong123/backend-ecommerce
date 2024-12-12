package com.shop.ecommerce.services;
import com.shop.ecommerce.models.*;
import com.shop.ecommerce.models.enums.ProductAttributeStatus;
import com.shop.ecommerce.models.enums.ProductStatus;
import com.shop.ecommerce.repositories.InventoryRepository;
import com.shop.ecommerce.repositories.SKUDetailsRepository;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.responses.ProductDetailResponse;
import com.shop.ecommerce.responses.SKUDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SKUDetailsServiceImpl implements SKUDetailsService {


    private final SKUDetailsRepository repository;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(SKUDetailsServiceImpl.class);
    private final InventoryRepository inventoryRepository;

    @Override
    public List<SKUDetailsResponse> getAllSKUDetails() {
        List<SKUDetails> drafts = repository.findAll();
            List<SKUDetailsResponse> SKUDetailsRes = drafts.stream().map(draft-> {
                Product product = productRepository.findById( draft.getProductId()).orElseThrow();
                return  SKUDetailsResponse.builder()
                        .id(draft.getId())
                        .productId(product.getId())
                        .productName(product.getName())
                        .urlImage(draft.getUrlImage())
                        .color(draft.getColor())
                        .size(draft.getSize())
                        .capacity(draft.getCapacity())
                        .price(draft.getPrice())
                        .discountPercent(draft.getDiscountPercent())
                        .build();
            }).toList();
        return SKUDetailsRes;
    }
    @Override
    public List<SKUDetailsResponse> findByStatus(ProductAttributeStatus status) {
        List<SKUDetails> drafts = repository.findByStatus(status);
        return drafts.stream().map(draft-> {
            Product product = productRepository.findById( draft.getProductId()).orElseThrow();
            return  SKUDetailsResponse.builder()
                    .id(draft.getId())
                    .productId(product.getId())
                    .productName(product.getName())
                    .urlImage(draft.getUrlImage())
                    .color(draft.getColor())
                    .size(draft.getSize())
                    .capacity(draft.getCapacity())
                    .price(draft.getPrice())
                    .discountPercent(draft.getDiscountPercent())
                    .build();
        }).toList();
    }



    @Override
    public SKUDetails createSKUDetails(SKUDetails skuDetails) {
        skuDetails.setStatus(ProductAttributeStatus.DRAFT);
        Product product = productRepository.findById(skuDetails.getProductId()).orElseThrow();;
        product.getImages().add(Image.builder()
                        .urlImage(skuDetails.getUrlImage())
                        .color(skuDetails.getColor())
                .build());
        productRepository.save(product);
        return repository.save(skuDetails);
    }

    @Override
    public SKUDetails updateSKUDetails(String id, SKUDetails SKUDetails) {
        Optional<SKUDetails> existingDraft = repository.findById(id);
        if (existingDraft.isPresent()) {
            SKUDetails.setId(id); // Đảm bảo ID không bị thay đổi
            return repository.save(SKUDetails);
        } else {
            throw new RuntimeException("SKUDetails with ID " + id + " not found.");
        }
    }


    @Override
    public SKUDetails updateStatus(String variationId, ProductAttributeStatus status) {
        SKUDetails SKUDetails = repository.findById(variationId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        SKUDetails.setStatus(status);
        return repository.save(SKUDetails);
    }

    @Override
    public ProductDetailResponse getProductStoredVariationId(String variantId) {
        SKUDetails skuDetails = repository.findById(variantId).orElseThrow();
        Product product = productRepository.findById(skuDetails.getProductId()).orElseThrow();;

        Inventory inventory = inventoryRepository.findByVariationId(variantId).orElseThrow();

        return ProductDetailResponse.builder()
                .id(product.getId())
                .variationId(variantId)
                .name(product.getName())
                .price(skuDetails.getPrice())
                .capacity(skuDetails.getCapacity())
                .color(skuDetails.getColor())
                .size(skuDetails.getSize())
                .discountPercent(skuDetails.getDiscountPercent())
                .quantity(inventory.getQuantityInStock())
                .urlImage(skuDetails.getUrlImage())
                .build();
    }

    @Override
    public SKUDetails updateSKUWithStoredStatus(SKUDetails updateStatus) {
        if (updateStatus == null || updateStatus.getId() == null) {
            throw new IllegalArgumentException("Invalid SKUDetails: ID cannot be null");
        }
        SKUDetails existingSKUDetails= repository.findById(updateStatus.getId())
                .orElseThrow(()-> new RuntimeException("SKUDetails not found with ID:" + updateStatus.getId()));

        existingSKUDetails.setPrice(updateStatus.getPrice());
        existingSKUDetails.setDiscountPercent(updateStatus.getDiscountPercent());
        existingSKUDetails.setStatus(ProductAttributeStatus.STORED);
        return repository.save(existingSKUDetails);
    }


    @Override
    public boolean isPublishedProduct(String variantId) {
        SKUDetails skuDetails = repository.findById(variantId).orElseThrow();
        Product product = productRepository.findById(skuDetails.getProductId()).orElseThrow();
        skuDetails.setStatus(ProductAttributeStatus.PUBLISHED);
        repository.save(skuDetails);

        product.setStatus(ProductStatus.PUBLISHED);
        product.getProductVariations().add(
                ProductVariation.builder()
                        .id(variantId)
                        .color(skuDetails.getColor())
                        .size(skuDetails.getSize())
                        .capacity(skuDetails.getCapacity())
                        .price(skuDetails.getPrice())
                        .discountPercent(skuDetails.getDiscountPercent())
                .build()
        );
        productRepository.save(product);


        return true;
    }
}
