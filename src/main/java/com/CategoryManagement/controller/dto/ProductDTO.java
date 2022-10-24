package com.CategoryManagement.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    Long id;
    String name;
    String description;
    String features;
    List<Long> categoryIdList;


}
