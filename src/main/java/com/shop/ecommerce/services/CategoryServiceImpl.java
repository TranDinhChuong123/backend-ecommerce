package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Category;
import com.shop.ecommerce.models.enums.CategoryStatus;
import com.shop.ecommerce.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category updateCategoryStatus(String categoryId, CategoryStatus categoryStatus) {
        Category category = getCategoryById(categoryId);
        category.setStatus(categoryStatus);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public List<Category> getCategoriesByStatus(CategoryStatus categoryStatus) {
        return categoryRepository.findByStatus(categoryStatus);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(String id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        existingCategory.setSlug(category.getSlug());
        existingCategory.setImageUrl(category.getImageUrl());
        existingCategory.setStatus(category.getStatus());
        existingCategory.setUpdatedAt(category.getUpdatedAt());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}