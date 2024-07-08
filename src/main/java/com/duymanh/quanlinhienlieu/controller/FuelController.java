package com.duymanh.quanlinhienlieu.controller;

import com.duymanh.quanlinhienlieu.DTO.CreateFuelRequest;
import com.duymanh.quanlinhienlieu.entity.Fuel;
import com.duymanh.quanlinhienlieu.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/fuels")
public class FuelController
{
    @Autowired
    private FuelService fuelService;

    @PostMapping("/create")
    public void createFuel(@RequestBody CreateFuelRequest createFuelRequest)
    {
        fuelService.createFuel(createFuelRequest);
    }

    @GetMapping
    public List<Fuel> getAllFuels()
    {
        return fuelService.getAllFuels();
    }

    @GetMapping("/fuels-bytimerange")
    public List<Fuel> getFuelsByTimeRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {
        return fuelService.getFuelsByTimeRange(startDate, endDate);
    }
}
