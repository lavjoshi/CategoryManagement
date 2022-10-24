package com.CategoryManagement.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDTO {

    @ApiModelProperty(notes = "Category name, should be unique.")
    String name;
    String parent;
}
