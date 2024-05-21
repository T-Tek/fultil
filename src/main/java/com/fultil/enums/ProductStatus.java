package com.fultil.enums;

public enum ProductStatus {
    IN_STOCK("IN_STOCK"),
    OUT_OF_STOCK("OUT_OF_STOCK");

    public final String value;

    ProductStatus(String value) {
        this.value = value;
    }
}