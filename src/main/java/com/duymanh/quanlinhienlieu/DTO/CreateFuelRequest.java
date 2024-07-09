package com.duymanh.quanlinhienlieu.DTO;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFuelRequest
{
    @Min(value = 1, message = "QUANTITY_INVALID")
    int quantity;
    LocalDate time;
    @Min(value = 1000, message = "PRICE_INVALID")
    double price;
    private int fuelTypeID;
    //private FuelType fuelType;
}