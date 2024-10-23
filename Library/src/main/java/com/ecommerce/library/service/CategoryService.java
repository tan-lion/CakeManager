package com.ecommerce.library.service;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();
    Category save(Category category);
    Optional<Category> findById(Integer id);
    Category update(Category category);
    void deleteById(Integer id);
    void enableById(Integer id);
    List<Category> findAllByActivatedTrue();

    List<CategoryDto> getCategoriesAndSize();
}
