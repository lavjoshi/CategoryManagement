package com.CategoryManagement.controller.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "Category path from root(main category)")
    String categoryPath;
}
