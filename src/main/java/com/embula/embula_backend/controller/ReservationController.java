package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.ReservationRequest;
import com.embula.embula_backend.dto.response.ReservationResponse;
import com.embula.embula_backend.entity.Reservation;
import com.embula.embula_backend.exception.ReservationConflictException;
import com.embula.embula_backend.services.ReservationService;
import com.embula.embula_backend.services.TableDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")

public class ReservationController {


    @Autowired
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> create(@Valid @RequestBody ReservationRequest req) {
        try {
            Reservation r = service.createReservation(req);
            ReservationResponse res = new ReservationResponse();
            res.setId(r.getId());
            res.setTableId(r.getTable().getId());
            res.setReservationStart(r.getReservationStart());
            res.setReservationEnd(r.getReservationEnd());
            res.setStatus(r.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (ReservationConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/tables/available")
    public ResponseEntity<?> available(
            @RequestParam String start,
            @RequestParam int members) {
        try {
            LocalDateTime s = LocalDateTime.parse(start);
            List<TableDto> tables = service.findAvailableTables(s, members);
            return ResponseEntity.ok(tables);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Invalid start datetime. Use ISO format: 2025-09-20T19:00:00");
        }
    }
//cancel update rservation
    @PutMapping("/reservations/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            service.cancelReservation(id);
            return ResponseEntity.ok("Reservation cancelled successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
