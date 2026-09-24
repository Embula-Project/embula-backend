package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.response.CustomerResponseDTO;
import com.embula.embula_backend.entity.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerResponseMappers {

    CustomerResponseDTO toResponseDTO(CustomerResponse customerResponse);
}
