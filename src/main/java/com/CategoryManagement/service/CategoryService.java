package com.CategoryManagement.service;

import com.CategoryManagement.controller.dto.CategoryDTO;
import com.CategoryManagement.controller.dto.CategoryResponseDTO;
import com.CategoryManagement.controller.dto.ResponseDTO;
import com.CategoryManagement.domain.Category;
import com.CategoryManagement.domain.Product;
import com.CategoryManagement.domain.ProductCategory;
import com.CategoryManagement.repository.CategoryRepositoryImpl;
import com.CategoryManagement.repository.ProductCategoryRepositoryImpl;
import com.CategoryManagement.util.ProductMappedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    CategoryRepositoryImpl categoryRepository;
    @Autowired
    ProductCategoryRepositoryImpl productCategoryRepository;

    @Autowired
    private Environment environment;


    /**
     * Add a new category with it's parent. If Parent is null, MAIN_CATEGORY is the default parent.
     * @param categoryDTO request body with category name and parent
     * @return CategoryResponseDTO with category path
     */
    @Transactional
    public CategoryResponseDTO insertCategory(CategoryDTO categoryDTO) {
        if (StringUtils.isEmpty(categoryDTO.getName())) {
            log.warn("Name is empty!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("Name is required!");
        }
        if (!StringUtils.isEmpty(categoryDTO.getParent()) && categoryDTO.getParent().equals(categoryDTO.getName())) {
            log.warn("New Category and parent cannot be identical!");
            throw new IllegalArgumentException("New Category and parent cannot be identical!");
        }
        Category parentCategory;
        if (StringUtils.isEmpty(categoryDTO.getParent())) {
            parentCategory = categoryRepository.findByName(environment.getProperty("MAIN_CATEGORY"));
            if (parentCategory == null) {
                parentCategory = Category.builder().name(environment.getProperty("MAIN_CATEGORY")).leftValue(1).rightValue(2).build();
                parentCategory = categoryRepository.save(parentCategory);
            }
        } else {
            parentCategory = categoryRepository.findByName(categoryDTO.getParent());
        }
        if (parentCategory == null) {
            log.warn("parent is null!, throwing NoSuchElementException");
            throw new NoSuchElementException("Parent does not exist");
        }
        Integer value;
        if (!StringUtils.isEmpty(categoryDTO.getParent()) && isLeafNode(categoryDTO.getParent())) {
            value = parentCategory.getLeftValue();
        } else {
            value = parentCategory.getRightValue() - 1;
        }
        categoryRepository.updateLeftValue(value);
        categoryRepository.updateRightValue(value);
        Category category = Category.builder().leftValue(value + 1).rightValue(value + 2).name(categoryDTO.getName()).build();
        category = categoryRepository.save(category);
        log.debug("Category added successfully");
        return CategoryResponseDTO.builder()
                .name(category.getName())
                .id(category.getId())
                .categoryPath(getCategoryTree(category.getId()))
                .build();
    }


    /**
     * Check with a category has no child
     * @param name category to check
     * @return True/False
     */
    private Boolean isLeafNode(String name) {
        Set<String> leafNodes = categoryRepository.getLeafNodes();
        if (leafNodes.contains(name)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Return category by name
     * @param name Name of category
     * @return CategoryResponseDTO with path
     */
    public CategoryResponseDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            log.warn("Not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("Category \'" + name + "\' does not exist");
        }
        String path = getCategoryTree(category.getId());
        return CategoryResponseDTO.builder().id(category.getId()).name(name).categoryPath(path).build();
    }

    /**
     * Returns category path from root
     * @param id Category ID
     * @return Path as String
     */
    public String getCategoryTree(Long id) {
        Set<String> tree = categoryRepository.getCategoryTreeById(id);
        return String.join(" -> ", tree);
    }

    /**
     * Update category name
     * @param name Old name
     * @param updatedName New name
     * @return ResponseDTO with error flag and message
     */
    public ResponseDTO updateCategory(String name, String updatedName) {
        Category category = categoryRepository.findByName(name);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(updatedName)) {
            log.warn("Name is empty!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("name and updatedName are required!");
        }
        if (name.equals(updatedName)) {
            log.warn("Name and updatedName are identical!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("name and updatedName are identical.");
        }
        if (category == null) {
            log.warn("Not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("Category \'" + name + "\' does not exist");
        }
        category.setName(updatedName);
        categoryRepository.save(category);
        log.debug("Updated successfully!");
        return ResponseDTO.builder().isError(Boolean.FALSE).message("Category has been updated!!").build();
    }

    /**
     * Delete a category by ID if no products are mapped to that category or category's child.
     * Deleting a category will also delete its child categories.
     * @param id Category ID
     * @return ResponseDTO with error flag and message
     */
    @Transactional
    public ResponseDTO deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            log.warn("Not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("Category \'" + id + "\' does not exist");
        }
        Category category = categoryOptional.get();
        if (category.getName().equals(environment.getProperty("MAIN_CATEGORY"))) {
            log.warn("Delete for MAIN_CATEGORY is not allowed!");
            throw new IllegalArgumentException("MAIN_CATEGORY cannot be deleted!");
        }
        Set<Long> categoriesToDelete = categoryRepository.getChildCategoryIDs(category.getLeftValue(), category.getRightValue());
        Set<ProductCategory> productCategorySet = productCategoryRepository.findAllByCategoryId(categoriesToDelete);

        if (productCategorySet != null && !productCategorySet.isEmpty()) {
            log.warn("Products are already mapped to category!");
            List<Long> ids = productCategorySet.stream().map(ProductCategory::getProduct).collect(Collectors.toSet()).stream().map(Product::getId).toList();
            throw new ProductMappedException("Category/child category " + categoriesToDelete + " is/are already mapped to products", ids);
        }
        categoryRepository.deleteAllById(categoriesToDelete);
        Integer width = category.getRightValue() - category.getLeftValue() + 1;
        categoryRepository.updateRightValue(width, category.getRightValue());
        categoryRepository.updateLeftValue(width, category.getRightValue());
        log.debug("Deleted successfully!");
        return ResponseDTO.builder().isError(Boolean.FALSE).message("Category(s) " + categoriesToDelete + " has/have been deleted!!").build();
    }


    /**
     * Fetch all categories in DB
     * @return List of Categories
     */
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
        for (Category category : categoryList) {
            Set<String> tree = categoryRepository.getCategoryTreeById(category.getId());
            categoryResponseDTOList.add(CategoryResponseDTO.builder().categoryPath(String.join(" -> ", tree))
                    .name(category.getName())
                    .id(category.getId())
                    .build());
        }
        return categoryResponseDTOList;
    }
}
