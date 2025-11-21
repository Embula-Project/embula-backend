package com.embula.embula_backend.dto.request;

import com.embula.embula_backend.entity.enums.MealType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

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

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    private Long tableId;
}
