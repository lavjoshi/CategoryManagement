package com.CategoryManagement.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    @ApiModelProperty(notes = "Required for updating.")
    Long id;
    @ApiModelProperty(notes = "Required")
    String name;
    @ApiModelProperty(notes = "Required")
    String description;
    String features;
    @ApiModelProperty(notes = "Required")
    List<Long> categoryIdList;


}
