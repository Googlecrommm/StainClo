package com.stainclo.category.repository;

import com.stainclo.category.entity.Category;
import com.stainclo.category.enums.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryName(String categoryName);
    boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Long categoryId);
    Page<Category> findByCategoryStatus(CategoryStatus categoryStatus, Pageable pageable);
}
