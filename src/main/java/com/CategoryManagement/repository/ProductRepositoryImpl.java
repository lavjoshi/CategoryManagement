package com.CategoryManagement.repository;

import com.CategoryManagement.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepositoryImpl extends JpaRepository<Product,Long> {


}
