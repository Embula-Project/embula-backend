package com.embula.embula_backend.controller;


import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestBody CustomerDTO customerDTO){
        String message=customerService.saveCustomer(customerDTO);
        return message;
    }

}
