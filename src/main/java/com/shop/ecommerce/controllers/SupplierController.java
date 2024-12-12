package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Supplier;
import com.shop.ecommerce.requests.CartRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SupplierController {
    private final SupplierService service;

    @PostMapping("/create")
    public ApiResponse<Boolean> createSupplier(@RequestBody Supplier supplier) {
        return new ApiResponse<>(
                200,
                "CreateSupplier successfully",
                service.createSupplier(supplier)
        );
    }

    @GetMapping("")
    public ApiResponse<List<Supplier>> getSuppliers() {
        return new ApiResponse<>(
                200,
                "GetSuppliers successfully",
                service.getSuppliers()
        );
    }
}