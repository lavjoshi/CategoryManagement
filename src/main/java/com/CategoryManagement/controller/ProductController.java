package com.CategoryManagement.controller;

import com.CategoryManagement.controller.dto.ProductDTO;
import com.CategoryManagement.controller.dto.ResponseDTO;
import com.CategoryManagement.domain.Product;
import com.CategoryManagement.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(name = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a new product.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public Product createProduct(@RequestBody ProductDTO productDTO) {
        log.debug("Insert new product :" + productDTO.toString());
        return productService.createUpdateNewProduct(productDTO, Boolean.FALSE);
    }

    @PutMapping(name = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update a product.", notes = "Change product description or add product to multiple category.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public Product updateProduct(@RequestBody ProductDTO productDTO) {
        log.debug("Insert new product :" + productDTO.toString());
        return productService.createUpdateNewProduct(productDTO, Boolean.TRUE);
    }

    @GetMapping(value = "/product/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a product by ID.", notes = "It also returns its category with full path.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public Product getProductById(@PathVariable Long productID) {
        log.debug("Get Product : " + productID);
        return productService.getProductById(productID);
    }

    @GetMapping(value = "/product/category/{categoryID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get products by category ID.", notes = "If recursive is True , the result will also contain products which have categoryID as parent")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public Set<Product> getActiveProductByCategory(@PathVariable Long categoryID, @RequestParam(required = false) Boolean recursive) {
        log.debug("Get Product : " + categoryID);
        return productService.getProductByCategory(categoryID, recursive);
    }


    @PutMapping(value = "/product/deactivate/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mark a product as inactive")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public Product markProductInactive(@PathVariable Long productID) {
        log.debug("Deactivate Product : " + productID);
        return productService.updateProductStatus(productID, Boolean.FALSE);
    }

    @PutMapping(value = "/product/activate/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mark a product as active")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public Product markProductActive(@PathVariable Long productID) {
        log.debug("Deactivate Product : " + productID);
        return productService.updateProductStatus(productID, Boolean.TRUE);
    }

    @DeleteMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete product by IDs")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public ResponseDTO deleteProduct(@RequestParam List<Long> productIDs) {
        log.debug("Delete Product : " + productIDs);
        return productService.deleteProduct(productIDs);
    }


}
