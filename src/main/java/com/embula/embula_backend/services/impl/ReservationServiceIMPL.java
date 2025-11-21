package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.ReservationRequestDto;
import com.embula.embula_backend.dto.response.ReservationResponseDto;
import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.entity.Reservation;
import com.embula.embula_backend.entity.RestaurantTable;
import com.embula.embula_backend.entity.enums.MealType;
import com.embula.embula_backend.entity.enums.ReservationStatus;
import com.embula.embula_backend.repository.ReservationRepository;
import com.embula.embula_backend.repository.RestaurantTableRepository;
import com.embula.embula_backend.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceIMPL implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;

    @Override
    public List<RestaurantTable> getAllAvailableTables() {
        return tableRepository.findByIsActive(true);
    }

    @Override
    public List<RestaurantTable> getAvailableTables(LocalDate date, MealType mealType, int guests) {
        return tableRepository.findAvailableTables(date, mealType, guests);
    }

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationDto) {
        List<RestaurantTable> availableTables = getAvailableTables(
                reservationDto.getDate(), reservationDto.getMealType(), reservationDto.getNumberOfGuests());

        if (availableTables.isEmpty()) {
            throw new NotFoundException("No available tables for the selected date, meal type, and number of guests.");
        }

        RestaurantTable assignedTable = availableTables.get(0);

        Reservation reservation = new Reservation();
        reservation.setCustomerName(reservationDto.getCustomerName());
        reservation.setCustomerEmail(reservationDto.getCustomerEmail());
        reservation.setCustomerPhone(reservationDto.getCustomerPhone());
        reservation.setDate(reservationDto.getDate());
        reservation.setMealType(reservationDto.getMealType());
        reservation.setNumberOfGuests(reservationDto.getNumberOfGuests());
        reservation.setTable(assignedTable);
        reservation.setStatus(ReservationStatus.BOOKED);

        Reservation savedReservation = reservationRepository.save(reservation);

        return mapToResponseDto(savedReservation);
    }

    @Override
    public List<ReservationResponseDto> findReservationsByCustomer(String email) {
        return reservationRepository.findByCustomerEmail(email).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private ReservationResponseDto mapToResponseDto(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(reservation.getId());
        dto.setCustomerName(reservation.getCustomerName());
        dto.setDate(reservation.getDate());
        dto.setMealType(reservation.getMealType());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setStatus(reservation.getStatus());
        dto.setTableNumber(reservation.getTable().getTableNumber());
        dto.setTableId(reservation.getTable().getId());
        return dto;
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + reservationId));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponseDto> findReservationsByDateAndMealType(LocalDate date, MealType mealType) {
        return reservationRepository.findByDateAndMealTypeAndStatus(date, mealType, ReservationStatus.BOOKED).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
}
