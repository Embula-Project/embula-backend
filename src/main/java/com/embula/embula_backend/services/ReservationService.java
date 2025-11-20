package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.ReservationRequest;
import com.embula.embula_backend.entity.Reservation;
import com.embula.embula_backend.services.TableDto;


import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationRequest req);
    List<TableDto> findAvailableTables(LocalDateTime start, int members);
    void cancelReservation(Long id);
    void updateReservation(Long id, ReservationRequest req);
}
