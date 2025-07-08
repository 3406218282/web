package com.restaurant.orderingsystem.service;

import com.restaurant.orderingsystem.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    
    Category createCategory(Category category);
    
    Optional<Category> getCategoryById(Long id);
    
    List<Category> getAllCategories();
    
    Category updateCategory(Long id, Category categoryDetails);
    
    void deleteCategory(Long id);
    
    boolean existsByName(String name);
} 