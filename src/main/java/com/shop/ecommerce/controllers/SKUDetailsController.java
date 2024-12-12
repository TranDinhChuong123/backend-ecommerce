package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.SKUDetails;
import com.shop.ecommerce.models.enums.ProductAttributeStatus;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.ProductDetailResponse;
import com.shop.ecommerce.responses.SKUDetailsResponse;
import com.shop.ecommerce.services.SKUDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sku-details")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SKUDetailsController {
    private final SKUDetailsService SKUDetailsService;

    @PostMapping("")
    public ApiResponse<SKUDetails> createSKUDetails(@RequestBody SKUDetails SKUDetails) {
        return new ApiResponse<>(
                "CreateSKUDetails successfully",
                SKUDetailsService.createSKUDetails(SKUDetails)
        );
    }

    @GetMapping("")
    public ApiResponse<List<SKUDetailsResponse>> getAllDrafts() {
        return new ApiResponse<>(
                "getAllDrafts successfully",
                SKUDetailsService.getAllSKUDetails()
        );
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<SKUDetailsResponse>> getSKUDetailssByStatus(@PathVariable ProductAttributeStatus status) {
        return new ApiResponse<>(
                "getAllDrafts successfully",
                SKUDetailsService.findByStatus(status)
        );
    }

    @PutMapping("/{variationId}/status")
    public ApiResponse<SKUDetails> updateStatus(
            @PathVariable("variationId") String variationId,
            @RequestParam("status") ProductAttributeStatus status
    ) {
        return new ApiResponse<>(
                200,
                "updateStatus successfully",
                SKUDetailsService.updateStatus(variationId, status)
        );
    }

    @GetMapping("/get-product/{variationId}")
    public ApiResponse<ProductDetailResponse> getProductStoredVariationId(
            @PathVariable String variationId
    ) {
        return new ApiResponse<>(
                200,
                "GetProductStoredVariationId successfully",
                SKUDetailsService.getProductStoredVariationId(variationId)
        );
    }

    @PostMapping("/publish/{variationId}")
    public ApiResponse<Boolean> isPublishedProduct(
            @PathVariable String variationId
    ) {
        return new ApiResponse<>(
                200,
                "Published Product successfully",
                SKUDetailsService.isPublishedProduct(variationId)
        );
    }

    @PutMapping("/stored")
    public ApiResponse<SKUDetails> updateSKU(@RequestBody SKUDetails skuDetails) {
        return new ApiResponse<>(
                200,
                "updateSKUDetails successfully",
                SKUDetailsService.updateSKUWithStoredStatus(skuDetails)
        );
    }







}
