package com.embula.embula_backend.dto.response;

import com.embula.embula_backend.entity.enums.MealType;
import com.embula.embula_backend.entity.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationResponseDto {
    private Long id;
    private String customerName;
    private LocalDate date;
    private MealType mealType;
    private int numberOfGuests;
    private ReservationStatus status;
    private String tableNumber;
    private Long tableId;
}
