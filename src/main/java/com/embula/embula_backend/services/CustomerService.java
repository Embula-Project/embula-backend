package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.entity.Customer;

public interface CustomerService {

    public String saveCustomer(CustomerDTO customerDTO);

}
