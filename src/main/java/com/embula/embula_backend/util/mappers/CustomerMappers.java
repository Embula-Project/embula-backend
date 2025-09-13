package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface CustomerMappers {

    Customer saveCustomer(CustomerDTO customerDTO);

}
