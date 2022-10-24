package com.CategoryManagement.repository;

import com.CategoryManagement.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductCategoryRepositoryImpl extends JpaRepository<ProductCategory, Long> {

    @Query("select p from ProductCategory p where p.categoryId in ?1")
    Set<ProductCategory> findAllByCategoryId(Set<Long> ids);

    @Query("delete from ProductCategory p where p.categoryId in ?1")
    @Modifying
    List<ProductCategory> deleteByCategoryIds(Set<Long> ids);
}
