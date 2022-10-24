package com.CategoryManagement.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseDTO {
    String message;
    Boolean isError;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Long> productIDList;

}
