package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.dto.response.ViewCustomerDTO;
import com.embula.embula_backend.entity.Customer;
import com.embula.embula_backend.repository.CustomerRepository;
import com.embula.embula_backend.services.CustomerService;
import com.embula.embula_backend.util.mappers.CustomerMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceIMPL implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMappers customerMappers;

    @Override
    public String saveCustomer(CustomerDTO customerDTO){
        Customer customer = customerMappers.saveCustomer(customerDTO);
        customerRepository.save(customer);
        return "Save Successfull with Id"+ customerDTO.getId();
    }

    @Override
    public List<ViewCustomerDTO> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> {
                    ViewCustomerDTO dto = new ViewCustomerDTO();
                    dto.setId(customer.getId());
                    dto.setFirstName(customer.getFirstName());
                    dto.setLastName(customer.getLastName());
                    dto.setEmail(customer.getEmail());
                    dto.setStatus(customer.getStatus());
                    dto.setAddress(customer.getAddress());
                    dto.setPhone(customer.getPhone());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
