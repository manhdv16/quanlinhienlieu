package com.duymanh.quanlinhienlieu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "fuel")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fuel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuel_id")
    int fuelID;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "time")
    LocalDate time;

    @Column(name = "price")
    double price;

    @Column(name = "fuel_type_id", insertable=false, updatable=false)
    int fuelTypeID;

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    @JsonBackReference("")
    FuelType fuelType;
}
