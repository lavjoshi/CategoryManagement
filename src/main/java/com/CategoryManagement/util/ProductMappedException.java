package com.CategoryManagement.util;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductMappedException extends RuntimeException {
    List<Long> ids;

    public ProductMappedException(String message, List<Long> productIds) {
        super(message);
        this.ids = productIds;
    }
}
