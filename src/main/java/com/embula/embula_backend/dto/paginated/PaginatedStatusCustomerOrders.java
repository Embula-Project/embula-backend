package com.embula.embula_backend.dto.paginated;

import com.embula.embula_backend.dto.response.StatusCustomerOrdersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedStatusCustomerOrders {
    List<StatusCustomerOrdersDTO> list;
    private Long total;


}
