package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Inventory;
import com.shop.ecommerce.requests.FilterRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.InventoryResponse;
import com.shop.ecommerce.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventoryController {
    private final InventoryService service;
    @PostMapping("")
    public ApiResponse<List<InventoryResponse>> findAllListInventory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FilterRequest request
    ) {
        return new ApiResponse<>(
                200,
                "findAllListInventory successfully",
                service.findAllListInventory(request)
        );
    }

    @GetMapping("/{productId}")
    public ApiResponse<List<InventoryResponse>> findInventoriesByProductID(
            @PathVariable String productId
    ) {
        return new ApiResponse<>(
                200,
                "findInventoriesByProductID successfully",
                service.findInventoriesByProductID(productId)
        );
    }




    @GetMapping("/sku/{sku}")
    public ApiResponse<Inventory> findByVariationId(
            @PathVariable String sku
    ) {
        return new ApiResponse<>(
                200,
                "findInventoriesByProductID successfully",
                service.findByVariationId(sku)
        );
    }
//    @PostMapping("/add-stock/{sku}")
//    public ApiResponse<Boolean> findByVariationId(
//            @PathVariable String sku,
//            @AuthenticationPrincipal UserDetails user
//    ) {
//        return new ApiResponse<>(
//                200,
//                "addQuantityInventory successfully",
//                service.addQuantityInventory(entry, sku)
//        );
//    }

    @PostMapping("/new")
    public ApiResponse<Inventory> addInventory(
            @RequestBody Inventory inventory,
            @AuthenticationPrincipal UserDetails user
    ) {

        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        return new ApiResponse<>(
                200,
                "addQuantityInventory successfully",
                service.addInventory(inventory)
        );
    }


}
