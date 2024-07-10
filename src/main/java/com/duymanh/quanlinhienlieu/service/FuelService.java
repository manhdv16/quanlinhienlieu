package com.duymanh.quanlinhienlieu.service;

import com.duymanh.quanlinhienlieu.DTO.CreateFuelRequest;
import com.duymanh.quanlinhienlieu.entity.Fuel;
import com.duymanh.quanlinhienlieu.entity.FuelType;
import com.duymanh.quanlinhienlieu.exception.AppException;
import com.duymanh.quanlinhienlieu.exception.ErrorCode;
import com.duymanh.quanlinhienlieu.repository.FuelRepository;
import com.duymanh.quanlinhienlieu.repository.FuelTypeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FuelService
{
    private static Logger LOGGER = LogManager.getLogger(FuelService.class);
    @Autowired
    FuelRepository fuelRepository;
    @Autowired
    FuelTypeRepository fuelTypeRepository;

    /**
     * Function is used to create Fuel obj
     * @param createFuelRequest
     * Check exception if fuel type not found
     * @return Fuel Object
     */
    public Fuel createFuel(CreateFuelRequest createFuelRequest)
    {
        Fuel fuel = new Fuel();

        fuel.setQuantity(createFuelRequest.getQuantity());
        fuel.setTime(createFuelRequest.getTime());
        fuel.setPrice(createFuelRequest.getPrice());

        Optional<FuelType> fuelTypeOpt = fuelTypeRepository.findById(createFuelRequest.getFuelTypeID());

        // Check fuelTypeID
        if(fuelTypeOpt.isEmpty())
        {
            LOGGER.error("Fuel type not found with type id: " + createFuelRequest.getFuelTypeID());
            throw new AppException(ErrorCode.FUEL_TYPE_NOT_FOUND);
        }
        fuel.setFuelType(fuelTypeOpt.get());
        return fuelRepository.save(fuel);
    }

    // Function is used to get all fuels
    public List<Fuel> getAllFuels()
    {
        return fuelRepository.findAll();
    }

    /**
     * Function is used to get fuels in time range
     * @param startDate
     * @param endDate
     * If endDate is before startDate, swap endDate and startDate
     * @return List<Fuel>
     */
    public List<Fuel> getFuelsByTimeRange(LocalDate startDate, LocalDate endDate)
    {

        if(endDate.isBefore(startDate))
        {
            LOGGER.error("End date is before start date");
            throw new AppException(ErrorCode.TIME_INVALID);
        }
        return fuelRepository.findByTimeBetweenOrderByTimeDesc(startDate, endDate);
    }

    /**
     * Function is used to get a map<fuelTypeID, List<Fuel>> and the input is a map<fuelTypeID, QuantityNeedToGet>
     * with condition total quantity fuels have the same fuelTypeID and total quantity < QuantityNeedToGet
     * @param data Map<Integer, Integer>
     * @return Map<fuelTypeID,List<Fuel>>
     */
    public Map<Integer,List<Fuel>> getConsumerFromDb(Map<Integer, Integer> data)
    {
        Map<Integer,List<Fuel>> consume = new HashMap<>();
        int quantity;
        for(int key : data.keySet())
        {
            // Total quantity by fuelTypeID
            quantity = fuelRepository.getQuantityByFuelTypeId(key);
            // Check condition
            if(quantity<data.get(key)) {
                // If total quantity < QuantityNeedToGet, return null
                consume.put(key, null);
            }else{
                // Get a list consumed fuels for each consumer
                List<Fuel> listRecordConsume = fuelRepository.getRecordConsume(key,data.get(key));
                consume.put(key, listRecordConsume);
            }
            LOGGER.info("Current state of consume map after processing key " + key + ": " + consume);
        }
        return consume;
    }
}
