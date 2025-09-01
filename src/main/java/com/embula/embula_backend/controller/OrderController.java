package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.services.OrderService;
import com.embula.embula_backend.util.StandardResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(path="saveOrder")
    public ResponseEntity<StandardResponse> saveOrder (OrderDTO orderDTO){
        String message = orderService.saveOrder(orderDTO);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );
        return responseEntity;
    }


    @GetMapping(
            path="viewAllOrders",
            params={"page","size"}
    )
    public ResponseEntity<StandardResponse> viewOrder(
            @RequestParam(value="page") int page,
            @RequestParam(value="size") int size


    ){
       // List<ViewOrderDTO> viewOrderDTO = orderService.viewAllOrders();
        PaginatedAllOrders paginatedAllOrders = orderService.viewAllOrders(page,size);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", paginatedAllOrders),
                HttpStatus.OK
        );

        return responseEntity;
    }


    @PutMapping(
            path="cancelOrder",
            params="orderId"
    )
    public ResponseEntity<StandardResponse> cancelorder(@RequestParam String orderId){
        String message= orderService.cancelOrder(orderId);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );

        return responseEntity;
    }
}
