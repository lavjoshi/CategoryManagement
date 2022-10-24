package com.CategoryManagement.service;

import com.CategoryManagement.controller.dto.ProductDTO;
import com.CategoryManagement.controller.dto.ResponseDTO;
import com.CategoryManagement.domain.Category;
import com.CategoryManagement.domain.Product;
import com.CategoryManagement.domain.ProductCategory;
import com.CategoryManagement.repository.CategoryRepositoryImpl;
import com.CategoryManagement.repository.ProductCategoryRepositoryImpl;
import com.CategoryManagement.repository.ProductRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    @Autowired
    CategoryRepositoryImpl categoryRepository;
    @Autowired
    ProductRepositoryImpl productRepository;

    @Autowired
    ProductCategoryRepositoryImpl productCategoryRepository;

    /**
     * Save or update a product with its product category
     *
     * @param productDTO request body
     * @param isUpdate   flag to insert or update
     * @return Product with categories
     */
    @Transactional
    public Product createUpdateNewProduct(ProductDTO productDTO, Boolean isUpdate) {
        if (StringUtils.isEmpty(productDTO.getName()) || StringUtils.isEmpty(productDTO.getDescription())) {
            log.warn("Name/desc is empty!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("Name and description is required!");
        }
        if (productDTO.getCategoryIdList() == null || productDTO.getCategoryIdList().isEmpty()) {
            log.warn("Category is empty!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("At least one category ID is required!");
        }
        if (isUpdate && (productDTO.getId() == null || productDTO.getId().equals(0L))) {
            log.warn("product ID is null!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("ID is required to update product!");
        }
        Product product;
        List<ProductCategory> productCategoryList = new ArrayList<>();
        List<Long> productCategoryIdsToDelete;
        if (isUpdate) {
            Optional<Product> result = productRepository.findById(productDTO.getId());
            if (result.isEmpty()) {
                log.warn("ProductId not found!, throwing IllegalArgumentException");
                throw new IllegalArgumentException("Invalid product ID");
            }
            product = result.get();
            productCategoryIdsToDelete = product.getProductCategoryList().stream().map(ProductCategory::getId).toList();
            product.setProductCategoryList(null);
            product.setDescription(productDTO.getDescription());
            product.setFeatures(productDTO.getFeatures());
            product.setName(productDTO.getName());
        } else {
            productCategoryIdsToDelete = null;
            product = Product.builder().name(productDTO.getName()).description(productDTO.getDescription()).features(productDTO.getFeatures()).isActive(Boolean.TRUE).build();
        }
        for (Long id : productDTO.getCategoryIdList()) {
            Set<String> categorySet = categoryRepository.getCategoryTreeById(id);
            if (categorySet == null || categorySet.isEmpty()) {
                throw new IllegalArgumentException("Category ID: " + id + " not found!");
            }
            productCategoryList.add(ProductCategory.builder().product(product).categoryId(id).categoryPath(String.join(" -> ", categorySet)).build());
        }
        product.setProductCategoryList(productCategoryList);
        product = productRepository.save(product);
        if (productCategoryIdsToDelete != null && !productCategoryIdsToDelete.isEmpty()) {
            productCategoryRepository.deleteAllById(productCategoryIdsToDelete);
        }
        return product;
    }

    public Product getProductById(Long productId) {
        if (productId == null || productId.equals(0L)) {
            log.warn("product ID is invalid!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("productID is required!");
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            log.warn("Product not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("productID not found!");
        }
        return product.get();
    }

    /**
     * Get all product for a given category.
     * If recursive is True, include products from category's child as well.
     *
     * @param categoryID category
     * @param recursive  flag to fetch child of a category
     * @return List of Product
     */
    public Set<Product> getProductByCategory(Long categoryID, Boolean recursive) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryID);
        if (categoryOptional.isEmpty()) {
            log.warn("Not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("Category ID: \'" + categoryID + "\' does not exist");
        }
        Category category = categoryOptional.get();
        Set<Long> categoryIds;
        if (recursive != null && recursive.equals(Boolean.TRUE)) {
            categoryIds = categoryRepository.getChildCategoryIDs(category.getLeftValue(), category.getRightValue());
        } else {
            categoryIds = new HashSet<>() {{
                add(categoryID);
            }};
        }
        Set<ProductCategory> productCategorySet = productCategoryRepository.findAllByCategoryId(categoryIds);
        if (productCategorySet.isEmpty()) {
            log.warn("Not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("No products found!");
        }
        return productCategorySet.stream().map(ProductCategory::getProduct).collect(Collectors.toSet());
    }

    /**
     * Update Product isActive flag
     *
     * @param productId ID of product to update
     * @param status    value of flag
     * @return Product with categories
     */
    public Product updateProductStatus(Long productId, Boolean status) {
        if (productId == null || productId.equals(0L)) {
            log.warn("productId is empty!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("productId is required!");
        }
        if (status == null) {
            log.warn("status is empty!, throwing IllegalArgumentException");
            throw new IllegalArgumentException("status is required!");
        }
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            log.warn("Product not found!, throwing NoSuchElementException");
            throw new NoSuchElementException("productId not found!");
        }
        Product product = productOptional.get();
        product.setIsActive(status);
        product = productRepository.save(product);
        return product;

    }

    /**
     * Deletes product and product category mapping for given product IDs
     *
     * @param productIDs IDs of product to delete
     * @return ResponseDTO with message and error flag
     */
    public ResponseDTO deleteProduct(List<Long> productIDs) {
        List<Product> productList = productRepository.findAllById(productIDs);
        if (productList.isEmpty()) {
            log.warn("productId is empty!, throwing NoSuchElementException");
            throw new NoSuchElementException("No product(s) found for given ID(s)!");
        }

        productRepository.deleteAll(productList);
        return ResponseDTO.builder().isError(Boolean.FALSE).message("Deleted successfully!")
                .productIDList(productList.stream().map(Product::getId).toList()).build();
    }
}
