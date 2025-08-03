package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.entity.Customer;
import com.embula.embula_backend.repository.CustomerRepository;
import com.embula.embula_backend.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceIMPL implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public String saveCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer(
                customerDTO.id,
                customerDTO.name,
                customerDTO.email,
                customerDTO.phone
        );
        customerRepository.save(customer);
        return "Save Successfull with Id"+ customerDTO.id;
    }
}
