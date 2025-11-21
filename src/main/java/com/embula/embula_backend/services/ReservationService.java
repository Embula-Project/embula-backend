package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.ReservationRequestDto;
import com.embula.embula_backend.dto.response.ReservationResponseDto;
import com.embula.embula_backend.entity.RestaurantTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationService {
    List<RestaurantTable> getAvailableTables(LocalDate date, LocalTime time, int guests);
    ReservationResponseDto createReservation(ReservationRequestDto reservationDto);
    List<ReservationResponseDto> findReservationsByCustomer(String email);
    void cancelReservation(Long reservationId);
}

