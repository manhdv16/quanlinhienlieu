package com.duymanh.quanlinhienlieu.repository;

import com.duymanh.quanlinhienlieu.entity.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Integer> {
    List<Fuel> findByTimeBetweenOrderByTimeDesc(LocalDate startDate, LocalDate endDate);
}
