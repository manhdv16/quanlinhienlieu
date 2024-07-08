package com.duymanh.quanlinhienlieu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "fuel_type")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FuelType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuel_type_id")
    int fuelTypeID;

    @Column(name = "fuel_type_name")
    String fuelTypeName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fuelType")
    @JsonManagedReference()
    Set<Fuel> fuelSet;
}
