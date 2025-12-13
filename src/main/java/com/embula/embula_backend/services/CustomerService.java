package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.dto.response.ViewCustomerDTO;

import java.util.List;

public interface CustomerService {

    public String saveCustomer(CustomerDTO customerDTO);

    public List<ViewCustomerDTO> getAllCustomers();

}
