package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.dto.request.AllPaymentDTO;
import com.embula.embula_backend.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMappers {
    Payment savePayments(PaymentDTO paymentDTO);
    List<AllPaymentDTO> paymentToAllPaymentDTO(List<Payment> payment);
}
