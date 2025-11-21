package com.embula.embula_backend.controller;

import com.embula.embula_backend.entity.Discount;
import com.embula.embula_backend.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/active")
    public List<Discount> getActiveDiscounts() {
        return discountService.getActiveDiscounts();
    }

    @GetMapping("/today")
    public List<Discount> getTodayDiscounts() {
        return discountService.getTodayDiscounts();
    }

    @PostMapping
    public Discount addDiscount(@RequestBody Discount discount) {
        return discountService.saveDiscount(discount);
    }
}
