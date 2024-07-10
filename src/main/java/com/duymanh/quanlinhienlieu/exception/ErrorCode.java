package com.duymanh.quanlinhienlieu.exception;

import lombok.Getter;

@Getter
public enum ErrorCode
{
    FUEL_TYPE_NOT_FOUND(404, "Not found fuel type"),
    QUANTITY_INVALID(400, "Invalid quantity"),
    PRICE_INVALID(400, "Invalid price"),
    TIME_NOT_FOUND(400, "Not found fuel"),
    TIME_INVALID(400, "End date is before start date")
    ;

    private final int code;
    private final String message;
    ErrorCode(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
