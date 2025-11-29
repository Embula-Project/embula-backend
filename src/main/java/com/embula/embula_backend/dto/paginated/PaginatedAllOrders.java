package com.embula.embula_backend.dto.paginated;


import com.embula.embula_backend.dto.response.ViewOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedAllOrders {
    List<ViewOrderDTO> list;
    private long totalOrders;

}
