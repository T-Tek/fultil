package com.fultil.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseCodeAndMessage {
    SUCCESS("200", "Success"),
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    INVALID_INPUT("422", "Invalid Input"),
    DUPLICATE_RECORD("409", "Duplicate Record"),
    RECORD_NOT_FOUND("410", "Record Not Found"),
    INVALID_AUTHENTICATION("511", "Invalid Authentication"),
    PRODUCT_NOT_FOUND("601", "Product Not Found"),
    INSUFFICIENT_STOCK("602", "Insufficient Stock"),
    INVALID_COUPON("603", "Invalid Coupon"),
    ORDER_ALREADY_PLACED("604", "Order Already Placed"),
    PAYMENT_FAILURE("605", "Payment Failure"),
    INVALID_SHIPPING_ADDRESS("606", "Invalid Shipping Address"),
    ORDER_CANCELLED("607", "Order Cancelled"),
    REFUND_SUCCESS("608", "Refund Successful"),
    REFUND_FAILED("609", "Refund Failed"),

    ALREADY_EXISTS("1", "Already Exists"),
    RESOURCE_NOT_FOUND("56", "Resource not Found");

    public final String code;
    public final String message;
}
