package com.duymanh.quanlinhienlieu.service;

import com.duymanh.quanlinhienlieu.DTO.CreateFuelRequest;
import com.duymanh.quanlinhienlieu.entity.Fuel;
import com.duymanh.quanlinhienlieu.entity.FuelType;
import com.duymanh.quanlinhienlieu.repository.FuelRepository;
import com.duymanh.quanlinhienlieu.repository.FuelTypeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FuelService
{
    @Autowired
    FuelRepository fuelRepository;
    @Autowired
    FuelTypeRepository fuelTypeRepository;

    public void createFuel(CreateFuelRequest createFuelRequest)
    {
        Fuel fuel = new Fuel();

        fuel.setQuantity(createFuelRequest.getQuantity());
        fuel.setTime(createFuelRequest.getTime());
        fuel.setPrice(createFuelRequest.getPrice());
        Optional<FuelType> fuelTypeOpt = fuelTypeRepository.findById(createFuelRequest.getFuelTypeID());
        if(fuelTypeOpt.isEmpty()) throw new RuntimeException("Not found fuel type");
        fuel.setFuelType(fuelTypeOpt.get());

        fuelRepository.save(fuel);
    }

    public List<Fuel> getAllFuels()
    {
        return fuelRepository.findAll();
    }

    public List<Fuel> getFuelsByTimeRange(LocalDate startDate, LocalDate endDate)
    {
        return fuelRepository.findByTimeBetweenOrderByTimeDesc(startDate, endDate);
    }
    public Map<Integer,List<Fuel>> getConsumerFromDb(Map<Integer, Integer> data) {
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
