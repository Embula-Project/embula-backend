package com.embula.embula_backend.dto.response;

import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for returning discount information to the client.
 * Includes all discount details including the image URL.
 */
@Data
public class DiscountResponseDto {

    private Long id;
    private String title;
    private String description;
    private Double discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean isActive;

    /**
     * URL/path to access the discount image.
     * Will be relative path like "/uploads/discounts/filename.jpg"
     */
    private String imageUrl;
}

