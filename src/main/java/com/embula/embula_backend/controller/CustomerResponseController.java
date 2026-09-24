package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.CustomerResponseRequestDTO;
import com.embula.embula_backend.dto.response.CustomerResponseDTO;
import com.embula.embula_backend.services.CustomerResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact-us")
@RequiredArgsConstructor
public class CustomerResponseController {

    private final CustomerResponseService customerResponseService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> submitCustomerResponse(@Valid @RequestBody CustomerResponseRequestDTO customerResponseRequestDTO) {
        CustomerResponseDTO responseDTO = customerResponseService.submitCustomerResponse(customerResponseRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
