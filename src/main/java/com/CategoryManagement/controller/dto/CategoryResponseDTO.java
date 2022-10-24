package com.CategoryManagement.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class CategoryResponseDTO {
    Long id;
    String name;
    String categoryPath;
}
