package com.embula.embula_backend.services;


import com.embula.embula_backend.entity.Discount;

import java.util.List;

public interface DiscountService {
    List<Discount> getAllDiscounts();
    List<Discount> getActiveDiscounts();
    List<Discount> getTodayDiscounts();
    Discount saveDiscount(Discount discount);
}

