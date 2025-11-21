package com.embula.embula_backend.services;

import com.embula.embula_backend.entity.Discount;

import java.util.List;

/**
 * Service interface for managing discount operations.
 * Handles CRUD operations and image uploads for discounts.
 */
public interface DiscountService {
    /**
     * Retrieves all discounts from the database.
     * @return List of all discounts
     */
    List<Discount> getAllDiscounts();

    /**
     * Retrieves only active discounts.
     * @return List of active discounts
     */
    List<Discount> getActiveDiscounts();

    /**
     * Retrieves discounts valid for today's date.
     * @return List of today's valid discounts
     */
    List<Discount> getTodayDiscounts();

    /**
     * Saves a discount entity (creates or updates).
     * @param discount Discount entity to save
     * @return Saved discount entity
     */
    Discount saveDiscount(Discount discount);
}
