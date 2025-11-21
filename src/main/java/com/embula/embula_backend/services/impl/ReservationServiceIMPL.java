package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.ReservationRequestDto;
import com.embula.embula_backend.dto.response.ReservationResponseDto;
import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.entity.Reservation;
import com.embula.embula_backend.entity.RestaurantTable;
import com.embula.embula_backend.entity.enums.ReservationStatus;
import com.embula.embula_backend.repository.ReservationRepository;
import com.embula.embula_backend.repository.RestaurantTableRepository;
import com.embula.embula_backend.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceIMPL implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RestaurantTable> getAvailableTables(LocalDate date, LocalTime time, int guests) {
        return tableRepository.findAvailableTables(date, time, guests);
    }

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationDto) {
        List<RestaurantTable> availableTables = getAvailableTables(
                reservationDto.getDate(), reservationDto.getTime(), reservationDto.getNumberOfGuests());

        if (availableTables.isEmpty()) {
            throw new NotFoundException("No available tables for the selected date, time, and number of guests.");
        }

        RestaurantTable assignedTable = availableTables.get(0); // Simple strategy: assign the first available table

        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);
        reservation.setTable(assignedTable);
        reservation.setStatus(ReservationStatus.BOOKED);

        Reservation savedReservation = reservationRepository.save(reservation);

        return modelMapper.map(savedReservation, ReservationResponseDto.class);
    }

    @Override
    public List<ReservationResponseDto> findReservationsByCustomer(String email) {
        return reservationRepository.findByCustomerEmail(email).stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + reservationId));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}

