package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.CustomerResponseRequestDTO;
import com.embula.embula_backend.dto.response.CustomerResponseDTO;

public interface CustomerResponseService {
    CustomerResponseDTO submitCustomerResponse(CustomerResponseRequestDTO customerResponseRequestDTO);
}
