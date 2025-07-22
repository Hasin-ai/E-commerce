package com.ecommerce.core.domain.product.repository;

import com.ecommerce.core.domain.product.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findBySlug(String slug);

    List<Category> findAll();

    List<Category> findByIsActiveTrue();

    List<Category> findByParentId(Long parentId);

    List<Category> findRootCategories();

    void delete(Category category);

    void deleteById(Long id);

    boolean existsBySlug(String slug);

    boolean hasSubcategories(Long categoryId);
}
