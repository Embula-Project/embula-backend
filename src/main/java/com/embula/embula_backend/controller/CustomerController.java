package com.embula.embula_backend.controller;


import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.dto.response.ViewCustomerDTO;
import com.embula.embula_backend.services.CustomerService;
import com.embula.embula_backend.util.StandardResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<StandardResponse> saveCustomer(@RequestBody CustomerDTO customerDTO){
        String message=customerService.saveCustomer(customerDTO);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );
        return responseEntity;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse> getCustomerById(@PathVariable String id){
        ViewCustomerDTO customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", customer),
                HttpStatus.OK
        );
    }

}
