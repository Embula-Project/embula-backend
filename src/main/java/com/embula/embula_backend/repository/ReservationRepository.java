package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Reservation;
import com.embula.embula_backend.entity.enums.MealType;
import com.embula.embula_backend.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerEmail(String email);
    List<Reservation> findByDateAndMealTypeAndStatus(LocalDate date, MealType mealType, ReservationStatus status);
}

