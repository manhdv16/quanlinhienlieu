package com.duymanh.quanlinhienlieu.repository;

import com.duymanh.quanlinhienlieu.entity.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Integer> {
    List<Fuel> findByTimeBetweenOrderByTimeDesc(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(f.quantity) FROM Fuel f WHERE f.fuelTypeID = ?1")
    int getQuantityByFuelTypeId(int fuelTypeId);

    @Query(value = "SELECT f1.fuel_id, f1.quantity, f1.time, f1.price, f1.fuel_type_id " +
            "FROM fuel f1 " +
            "JOIN ( " +
            "    SELECT f.fuel_id, " +
            "           SUM(f.quantity) OVER (ORDER BY f.time DESC) AS running_total " +
            "    FROM fuel f " +
            "    WHERE f.fuel_type_id = ?1 " +
            ") AS temp ON f1.fuel_id = temp.fuel_id " +
            "WHERE temp.running_total - f1.quantity < ?2 " +
            "ORDER BY f1.fuel_id", nativeQuery = true)
    List<Fuel> getRecordConsume(int fuelTypeId, int quantity);


}
