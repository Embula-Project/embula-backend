package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.RestaurantTable;
import com.embula.embula_backend.entity.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByIsActive(boolean isActive);

    @Query("SELECT t FROM RestaurantTable t WHERE t.capacity >= :guests AND t.id NOT IN (" +
           "SELECT r.table.id FROM Reservation r WHERE r.date = :date AND r.mealType = :mealType AND r.status = 'BOOKED')")
    List<RestaurantTable> findAvailableTables(LocalDate date, MealType mealType, int guests);
}
