package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.ReservationRequestDto;
import com.embula.embula_backend.dto.response.ReservationResponseDto;
import com.embula.embula_backend.entity.RestaurantTable;
import com.embula.embula_backend.entity.enums.MealType;
import com.embula.embula_backend.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/available-tables")
    public ResponseEntity<List<RestaurantTable>> getAllAvailableTables() {
        List<RestaurantTable> tables = reservationService.getAllAvailableTables();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam MealType mealType,
            @RequestParam int guests) {
        List<RestaurantTable> tables = reservationService.getAvailableTables(date, mealType, guests);
        return ResponseEntity.ok(tables);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto reservationDto) {
        ReservationResponseDto createdReservation = reservationService.createReservation(reservationDto);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCustomer(@RequestParam String email) {
        List<ReservationResponseDto> reservations = reservationService.findReservationsByCustomer(email);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<List<ReservationResponseDto>> checkReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam MealType mealType) {
        List<ReservationResponseDto> reservations = reservationService.findReservationsByDateAndMealType(date, mealType);
        return ResponseEntity.ok(reservations);
    }
}
