package com.embula.embula_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating/updating discount information.
 * Used when accepting JSON data from the client.
 */
@Data
public class DiscountRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount percentage must be at least 0")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private Double discountPercentage;

    @NotNull(message = "Valid from date is required")
    private LocalDate validFrom;

    @NotNull(message = "Valid to date is required")
    private LocalDate validTo;

    private Boolean isActive = true;

    /**
     * Optional image URL for the discount.
     * Can be provided in JSON or uploaded separately via multipart endpoint.
     */
    private String imageUrl;
}

