package com.restaurant.orderingsystem.service.impl;

import com.restaurant.orderingsystem.entity.Category;
import com.restaurant.orderingsystem.repository.CategoryRepository;
import com.restaurant.orderingsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
} 