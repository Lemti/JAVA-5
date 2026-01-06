package com.store.products.service;

import com.store.products.dao.CategoryDAO;
import com.store.products.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public Category createCategory(Category category) {
        return categoryDAO.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category updateCategory(Long id, Category updated) {
        Category existing = getCategoryById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return categoryDAO.save(existing);
    }

    public void deleteCategory(Long id) {
        categoryDAO.deleteById(id);
    }
}
