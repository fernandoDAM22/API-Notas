package com.notes.notes.service;

import com.notes.notes.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    List<Category> getAllByUser(Long id);
    Category getUserCategory(String name, Long id);
    Category findById(Long id);
    Category findByName(String name);
    Category save(Category category);
    Category delete(Long id);
    Category update(Category category);
}
