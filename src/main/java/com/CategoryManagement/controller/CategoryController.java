package com.CategoryManagement.controller;

import com.CategoryManagement.controller.dto.CategoryDTO;
import com.CategoryManagement.controller.dto.CategoryResponseDTO;
import com.CategoryManagement.controller.dto.ResponseDTO;
import com.CategoryManagement.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a new Category based on parent.", notes = "If parent is null, MAIN_CATEGORY is the default parent. MAIN_CATEGORY is set as part of env variable")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public CategoryResponseDTO insertCategory(@RequestBody CategoryDTO categoryDTO){
        log.debug("Insert Category : " + categoryDTO.toString());
        return categoryService.insertCategory(categoryDTO);

    }

    @GetMapping(value = "/category/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a Category.", notes = "It also returns full category path.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public CategoryResponseDTO getCategory(@PathVariable String name){
        log.debug("Get Category : " + name);
        return categoryService.getCategoryByName(name);

    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a Category.", notes = "It also returns full category path.")
    public List<CategoryResponseDTO> getAllCategory(){
        log.debug("Get all categories");
        return categoryService.getAllCategories();

    }

    @PutMapping(value = "/category/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates a Category.", notes = "Only name can be updated.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public ResponseDTO updateCategory(@PathVariable String name, @RequestParam(name = "updatedName") String updatedName){
        log.debug("Update category: " + name + " --> " + updatedName);
        return categoryService.updateCategory(name, updatedName);

    }

    @DeleteMapping(value = "/category/{categoryID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Deletes a Category if no products are mapped to it.", notes = "If a parent is deleted, all child categories will also be deleted.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request!")})
    public ResponseDTO deleteCategory(@PathVariable Long categoryID){
        log.debug("Delete category: " + categoryID );
        return categoryService.deleteCategory(categoryID);

    }



}
