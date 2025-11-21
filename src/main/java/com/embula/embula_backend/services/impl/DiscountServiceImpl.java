package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.entity.Discount;
import com.embula.embula_backend.repository.DiscountRepository;
import com.embula.embula_backend.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    public List<Discount> getActiveDiscounts() {
        return discountRepository.findByIsActiveTrue();
    }

    @Override
    public List<Discount> getTodayDiscounts() {
        LocalDate today = LocalDate.now();
        return discountRepository.findByValidFromLessThanEqualAndValidToGreaterThanEqual(today, today);
    }

    @Override
    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }
}
