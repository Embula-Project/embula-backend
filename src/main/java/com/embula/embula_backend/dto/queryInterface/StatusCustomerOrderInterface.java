package com.embula.embula_backend.dto.queryInterface;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public interface StatusCustomerOrderInterface {

    String getFirstName();
    String getLastName();
    String getPhone();
    String getEmail();
    String getOrderName();
    LocalDateTime getOrderCreatedDate();

}
