package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.CustomerResponseRequestDTO;
import com.embula.embula_backend.dto.response.CustomerResponseDTO;
import com.embula.embula_backend.entity.CustomerResponse;
import com.embula.embula_backend.repository.CustomerResponseRepository;
import com.embula.embula_backend.services.CustomerResponseService;
import com.embula.embula_backend.services.EmailService;
import com.embula.embula_backend.util.mappers.CustomerResponseMappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerResponseServiceIMPL implements CustomerResponseService {

    private final CustomerResponseRepository customerResponseRepository;
    private final CustomerResponseMappers customerResponseMappers;
    private final EmailService emailService;

    @Override
    @Transactional
    public CustomerResponseDTO submitCustomerResponse(CustomerResponseRequestDTO customerResponseRequestDTO) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setInquiryId(UUID.randomUUID().toString());
        customerResponse.setName(customerResponseRequestDTO.getName());
        customerResponse.setEmail(customerResponseRequestDTO.getEmail());
        customerResponse.setPhone(customerResponseRequestDTO.getPhone());
        customerResponse.setComplaintType(customerResponseRequestDTO.getComplaintType());
        customerResponse.setDescription(customerResponseRequestDTO.getDescription());
        customerResponse.setCreatedAt(LocalDateTime.now());

        CustomerResponse savedCustomerResponse = customerResponseRepository.save(customerResponse);

        emailService.sendCustomerResponseNotificationEmail(customerResponseRequestDTO);

        return customerResponseMappers.toResponseDTO(savedCustomerResponse);
    }
}
