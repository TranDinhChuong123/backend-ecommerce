package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.requests.ProductFilterRequest;
import com.shop.ecommerce.requests.ProductRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.StockVariationResponse;
import com.shop.ecommerce.services.InventoryService;
import com.shop.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    private final ProductService productService;
    private final InventoryService inventoryService;


    @GetMapping("/top-selling")
    public ApiResponse<List<Product>> getTopSellingProducts(@RequestParam(defaultValue = "10") int limit) {
        // Lấy danh sách sản phẩm bán chạy từ service
        List<Product> products = productService.getTopSellingProducts(limit);
        return new ApiResponse<>(
                201,
                "getTopSellingProducts successfully",
                products
        );
    }

    @PostMapping("/create")
    public ApiResponse<Product> addProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ApiResponse<>(
                201,
                "Created Product successfuly",
                productService.addProduct(productRequest)
        );
    }

    @PostMapping("")
    public ApiResponse<List<Product>> findProductsWithFilters(
            @RequestBody ProductFilterRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = (userDetails != null) ? userDetails.getUsername() : null;
        return new ApiResponse<>(
                "Products retrieved successfully",
                productService.findProductsWithFilters(request,userId)


        );
    }

    @PostMapping("/category-slug")
    public ApiResponse<List<Product>> findProductsWithCategoryAndKeywordFilter(@RequestBody ProductFilterRequest request) {
        return new ApiResponse<>(
                "Products retrieved successfully",
                productService.findProductsWithCategoryAndKeywordFilter(request)


        );
    }
    @GetMapping("/{productId}")
    public ApiResponse<Product> findProductById(
            @PathVariable String productId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = (userDetails != null) ? userDetails.getUsername() : null;
        Product product = productService.findProductById(productId, userId);
        return new ApiResponse<>(
                "Product found successfully",
                product
        );
    }

    @GetMapping("/drafts")
    public ApiResponse<List<Product>> findProductsDraft() {
        return new ApiResponse<>(
                "Product Draft found successfully",
                productService.findProductsDraft()
        );
    }

    @GetMapping("/{productId}/variations")
    public ApiResponse<List<StockVariationResponse>> getStockVariationByProductId(
            @PathVariable String productId
    ) {
        return new ApiResponse<>(
                200,
                "findInventoriesByProductID successfully",
                inventoryService.getStockVariationByProductId(productId)
        );
    }




}
