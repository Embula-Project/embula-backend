package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    @Query("SELECT t FROM RestaurantTable t WHERE t.capacity >= :guests AND t.id NOT IN (" +
           "SELECT r.table.id FROM Reservation r WHERE r.date = :date AND r.time = :time AND r.status = 'BOOKED')")
    List<RestaurantTable> findAvailableTables(LocalDate date, LocalTime time, int guests);
}

