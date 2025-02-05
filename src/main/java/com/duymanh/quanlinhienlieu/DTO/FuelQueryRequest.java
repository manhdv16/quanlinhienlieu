package com.duymanh.quanlinhienlieu.DTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FuelQueryRequest
{
    LocalDate startDate;
    LocalDate endDate;
}
