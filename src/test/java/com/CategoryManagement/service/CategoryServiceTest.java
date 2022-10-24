package com.CategoryManagement.service;

import com.CategoryManagement.controller.dto.CategoryDTO;
import com.CategoryManagement.repository.CategoryRepositoryImpl;
import com.CategoryManagement.repository.ProductCategoryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepositoryImpl categoryRepository;
    @Mock
    ProductCategoryRepositoryImpl productCategoryRepository;

    private CategoryDTO categoryDTO;
    @BeforeEach
    public void setUp() {
        categoryDTO = CategoryDTO.builder().name("").build();
    }

    @Test()
    public void testSaveCategoryWithEmptyName(){
     assertThrows(IllegalArgumentException.class,
             () -> categoryService.insertCategory(categoryDTO), "Expected insertCategory() to throw, but it didn't");
    }


}