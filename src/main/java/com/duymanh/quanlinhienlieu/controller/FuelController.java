package com.duymanh.quanlinhienlieu.controller;

import com.duymanh.quanlinhienlieu.DTO.ConsumerRequest;
import com.duymanh.quanlinhienlieu.DTO.CreateFuelRequest;
import com.duymanh.quanlinhienlieu.entity.Fuel;
import com.duymanh.quanlinhienlieu.exception.AppException;
import com.duymanh.quanlinhienlieu.exception.ErrorCode;
import com.duymanh.quanlinhienlieu.response.ApiResponse;
import com.duymanh.quanlinhienlieu.service.FuelService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1.0/fuels")
public class FuelController
{
    private static Logger LOGGER = LogManager.getLogger(FuelController.class);
    @Autowired
    private FuelService fuelService;

    /**
     * Creates a new fuel entry.
     *
     * @param createFuelRequest the request object containing details for the fuel to be created
     * @return ResponseEntity containing a success message
     */
    @PostMapping
    public ResponseEntity<?> createFuel(@RequestBody @Valid CreateFuelRequest createFuelRequest)
    {
        Fuel fuel = fuelService.createFuel(createFuelRequest);
        ApiResponse<String> response = new ApiResponse<>(200,"created successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @return ResponseEntity containing a list of all fuels
     */
    @GetMapping
    public  ResponseEntity<?> getAllFuels()
    {
        List<Fuel> listFuel = fuelService.getAllFuels();
        ApiResponse<List<Fuel>> response = new ApiResponse<>(200,"Get all fuels successfully", listFuel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves fuel entries within a specified time range and aggregates quantities by fuel type.
     *
     * @param startDate the start date of the time range
     * @param endDate the end date of the time range
     * @return ResponseEntity containing a map of fuel types and their total quantities within the time range
     * @throws AppException if no fuel entries are found within the specified time range
     */
    @GetMapping("/fuels-bytimerange")
    public ResponseEntity<?>  getFuelsByTimeRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {

        List<Fuel> listFuel = fuelService.getFuelsByTimeRange(startDate, endDate);
        if(listFuel.isEmpty()){
            LOGGER.error("Time not found with start date: ",startDate," and end date: ",endDate);
            throw new AppException(ErrorCode.TIME_NOT_FOUND);
        }
        // Create a map to store fuel type and total quantity
        Map<String, Integer> mapTypeFuel = new HashMap<>();
        String type;
        for(Fuel f : listFuel) {
            type = f.getFuelType().getFuelTypeName();
            // Check if mapTypeFuel contains key type
            if(mapTypeFuel.containsKey(type)){
                mapTypeFuel.put(type, mapTypeFuel.get(type)+f.getQuantity());
            }else{
                mapTypeFuel.put(type,f.getQuantity());
            }
            LOGGER.info("Current state of type fuel map after a processing" + type + ": " + mapTypeFuel.values());
        }
        ApiResponse<Map<String, Integer> > response = new ApiResponse<>(200,"Get fuels by time range successfully", mapTypeFuel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves consumed fuels for each consumer.
     *
     * @param request the request object containing consumer details
     * @return ResponseEntity containing a map of consumer IDs and their corresponding list of consumed fuels
     */
    @GetMapping("/consume")
    public ResponseEntity<?> getConsumer(@RequestBody ConsumerRequest request) {
        // Get a list consumed fuels for each consumer
        Map<Integer,List<Fuel>>mapFuel = fuelService.getConsumerFromDb(request.getMap());

        ApiResponse<Map<Integer,List<Fuel>>> response = new ApiResponse<>(200,"Get a list of successfully consumed fuels", mapFuel);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
