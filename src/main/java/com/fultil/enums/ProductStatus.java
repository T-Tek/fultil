package com.fultil.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    IN_STOCK("In stock"),
    OUT_OF_STOCK("Out of stock");

    private final String message;

    ProductStatus(String message) {
        this.message = message;
    }
}
