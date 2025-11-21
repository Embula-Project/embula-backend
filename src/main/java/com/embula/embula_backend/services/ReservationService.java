package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.ReservationRequestDto;
import com.embula.embula_backend.dto.response.ReservationResponseDto;
import com.embula.embula_backend.entity.RestaurantTable;
import com.embula.embula_backend.entity.enums.MealType;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    List<RestaurantTable> getAllAvailableTables();
    List<RestaurantTable> getAvailableTables(LocalDate date, MealType mealType, int guests);
    ReservationResponseDto createReservation(ReservationRequestDto reservationDto);
    List<ReservationResponseDto> findReservationsByCustomer(String email);
    void cancelReservation(Long reservationId);
    List<ReservationResponseDto> findReservationsByDateAndMealType(LocalDate date, MealType mealType);
}
