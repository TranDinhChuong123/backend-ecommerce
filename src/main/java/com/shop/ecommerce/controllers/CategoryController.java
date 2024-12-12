package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Category;
import com.shop.ecommerce.models.enums.CategoryStatus;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ApiResponse<List<Category>> getAllCategories() {
        return new ApiResponse<>(
                200,
                "Categories retrieved successfully",
                categoryService.getAllCategories()
        );
    }



    @GetMapping("/{id}")
    public ApiResponse<Category> getCategoryById(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id);
        String message = category != null
                ? "Category retrieved successfully"
                : "Category not found";
        return new ApiResponse<>(200, message, category);
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<Category> getCategoryBySlug(@PathVariable String slug) {
        Category category = categoryService.getCategoryBySlug(slug);
        String message = category != null
                ? "Category retrieved successfully by slug"
                : "Category not found";
        return new ApiResponse<>(200, message, category);
    }

    @PostMapping("/create")
    public ApiResponse<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return new ApiResponse<>(
                201,
                "Category created successfully",
                createdCategory
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> updateCategory(
            @PathVariable String id,
            @RequestBody Category category
    ) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return new ApiResponse<>(
                200,
                "Category updated successfully",
                updatedCategory
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return new ApiResponse<>(
                204,
                "Category deleted successfully",
                null
        );
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<Category>> getCategoriesByStatus(@PathVariable CategoryStatus status) {
        List<Category> categories = categoryService.getCategoriesByStatus(status);
        String message = categories.isEmpty()
                ? "No categories found with the specified status"
                : "Categories retrieved successfully by status";
        return new ApiResponse<>(200, message, categories);
    }

}
