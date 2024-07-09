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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FuelService
{
    @Autowired
    FuelRepository fuelRepository;
    @Autowired
    FuelTypeRepository fuelTypeRepository;

    public Fuel createFuel(CreateFuelRequest createFuelRequest)
    {
        Fuel fuel = new Fuel();

        fuel.setQuantity(createFuelRequest.getQuantity());
        fuel.setTime(createFuelRequest.getTime());
        fuel.setPrice(createFuelRequest.getPrice());
        Optional<FuelType> fuelTypeOpt = fuelTypeRepository.findById(createFuelRequest.getFuelTypeID());
        if(fuelTypeOpt.isEmpty()) throw new AppException(ErrorCode.FUEL_TYPE_NOT_FOUND);
        fuel.setFuelType(fuelTypeOpt.get());

        return fuelRepository.save(fuel);
    }

    public List<Fuel> getAllFuels()
    {
        return fuelRepository.findAll();
    }

    public List<Fuel> getFuelsByTimeRange(LocalDate startDate, LocalDate endDate)
    {
        if(endDate.isBefore(startDate))
        {
            return fuelRepository.findByTimeBetweenOrderByTimeDesc(endDate, startDate);
        }
        return fuelRepository.findByTimeBetweenOrderByTimeDesc(startDate, endDate);
    }
    public Map<Integer,List<Fuel>> getConsumerFromDb(Map<Integer, Integer> data)
    {
        Map<Integer,List<Fuel>> consume = new HashMap<>();
        for(int key : data.keySet()) {
            int quantity = fuelRepository.getQuantityByFuelTypeId(key);
            if(quantity<data.get(key)){
                consume.put(key, null);
            }else{
                List<Fuel> listRecordConsume = fuelRepository.getRecordConsume(key,data.get(key));
                consume.put(key, listRecordConsume);
            }
        }
        return consume;
    }
}
