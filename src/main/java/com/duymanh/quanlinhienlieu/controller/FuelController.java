package com.duymanh.quanlinhienlieu.controller;

import com.duymanh.quanlinhienlieu.DTO.ConsumerRequest;
import com.duymanh.quanlinhienlieu.DTO.CreateFuelRequest;
import com.duymanh.quanlinhienlieu.entity.Fuel;
import com.duymanh.quanlinhienlieu.response.ApiResponse;
import com.duymanh.quanlinhienlieu.response.FuelResponse;
import com.duymanh.quanlinhienlieu.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1.0/fuels")
public class FuelController
{
    @Autowired
    private FuelService fuelService;

    @PostMapping("/create")
    public ResponseEntity<?> createFuel(@RequestBody CreateFuelRequest createFuelRequest)
    {
        fuelService.createFuel(createFuelRequest);
        ApiResponse<String> response = new ApiResponse<>(200,"created successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<?> getAllFuels()
    {
        List<Fuel> listFuel = fuelService.getAllFuels();
        ApiResponse<List<Fuel>> response = new ApiResponse<>(200,"Get all fuels successfully", listFuel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/fuels-bytimerange")
    public ResponseEntity<?>  getFuelsByTimeRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {
        List<Fuel> listFuel = fuelService.getFuelsByTimeRange(startDate, endDate);
        Map<String, Integer> mapTypeFuel = new HashMap<>();
        for(Fuel f : listFuel) {
            String type = f.getFuelType().getFuelTypeName();
            if(mapTypeFuel.containsKey(type)){
                int quantity = mapTypeFuel.get(type)+f.getQuantity();
                mapTypeFuel.put(type,quantity);
            }else{
                mapTypeFuel.put(type,f.getQuantity());
            }
        }
        ApiResponse<Map<String, Integer> > response = new ApiResponse<>(200,"Get fuels by time range successfully", mapTypeFuel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/consume")
    public ResponseEntity<?> getConsumer(@RequestBody ConsumerRequest request) {
        Map<Integer, Integer> dataConsume = request.getMap();
        Map<Integer,List<Fuel>>mapFuel = fuelService.getConsumerFromDb(dataConsume);
        ApiResponse<Map<Integer,List<Fuel>>> response = new ApiResponse<>(200,"Get a list of successfully consumed fuels", mapFuel);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
