package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Category;
import com.shop.ecommerce.models.enums.CategoryStatus;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(String id);
    Category getCategoryBySlug(String slug);

    Category updateCategoryStatus(String categoryId, CategoryStatus categoryStatus);

    List<Category> getCategoriesByStatus(CategoryStatus status);
    Category createCategory(Category category);
    Category updateCategory(String id, Category category);
    void deleteCategory(String id);
}
