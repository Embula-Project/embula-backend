package com.embula.embula_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequestDto {

    @NotBlank(message = "Customer name cannot be empty")
    private String customerName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String customerEmail;

    @NotBlank(message = "Phone number cannot be empty")
    private String customerPhone;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Reservation date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    private Long tableId;
}

