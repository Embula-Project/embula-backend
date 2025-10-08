package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.ReservationRequest;
import com.embula.embula_backend.entity.*;
import com.embula.embula_backend.exception.ReservationConflictException;
import com.embula.embula_backend.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    private LocalDateTime computeEnd(LocalDateTime start, LocalDateTime providedEnd, String mealType) {
        if (providedEnd != null) return providedEnd;
        // Basic logic: lunch 90 mins, dinner 120 mins. Adjust as needed.
        if ("LUNCH".equalsIgnoreCase(mealType)) return start.plusMinutes(90);
        return start.plusMinutes(120);
    }

    @Transactional
    public Reservation createReservation(ReservationRequest req) {
        LocalDateTime start = req.getReservationStart();
        if (start == null) throw new IllegalArgumentException("reservationStart is required");
        LocalDateTime end = computeEnd(start, req.getReservationEnd(), req.getMealType());
        int members = req.getMembers();

        // If tableId provided => check that specific table is free
        if (req.getTableId() != null) {
            Long tableId = req.getTableId();
            // lock the table row to avoid concurrent assignment
            RestaurantTable table = tableRepository.findByIdForUpdate(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("Table not found"));
            List<Reservation> conflicts = reservationRepository.findConflictsForTable(tableId, start, end);
            if (!conflicts.isEmpty()) {
                throw new ReservationConflictException("Table already booked for that time");
            }
            Reservation r = new Reservation();
            r.setTable(table);
            r.setReservationStart(start);
            r.setReservationEnd(end);
            r.setMembers(members);
            r.setMealType(req.getMealType());
            r.setCustomerName(req.getCustomerName());
            r.setCustomerPhone(req.getCustomerPhone());
            r.setStatus("CONFIRMED");
            return reservationRepository.save(r);
        } else {
            // find booked table ids for interval, then find tables that fit members and aren't booked
            List<Long> bookedIds = reservationRepository.findReservedTableIds(start, end);
            int bookedSize = bookedIds == null ? 0 : bookedIds.size();
            List<RestaurantTable> available = tableRepository.findAvailableTables(members, bookedIds == null ? List.of() : bookedIds, bookedSize);
            if (available.isEmpty()) {
                throw new ReservationConflictException("No table available for requested time");
            }
            // Pick best-fit table (smallest capacity that fits)
            RestaurantTable chosen = available.get(0);
            // lock the chosen table row before creating reservation to avoid race
            RestaurantTable locked = tableRepository.findByIdForUpdate(chosen.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Table not found after selection"));
            // check again for conflicts for locked table
            List<Reservation> conflicts = reservationRepository.findConflictsForTable(locked.getId(), start, end);
            if (!conflicts.isEmpty()) {
                // someone else took it in the meantime
                throw new ReservationConflictException("Table became unavailable; try again");
            }
            Reservation r = new Reservation();
            r.setTable(locked);
            r.setReservationStart(start);
            r.setReservationEnd(end);
            r.setMembers(members);
            r.setMealType(req.getMealType());
            r.setCustomerName(req.getCustomerName());
            r.setCustomerPhone(req.getCustomerPhone());
            r.setStatus("CONFIRMED");
            return reservationRepository.save(r);
        }
    }

    public List<TableDto> findAvailableTables(LocalDateTime start, int members) {
        LocalDateTime end = computeEnd(start, null, "DINNER");
        List<Long> booked = reservationRepository.findReservedTableIds(start, end);
        int bookedSize = booked == null ? 0 : booked.size();
        List<RestaurantTable> tables = tableRepository.findAvailableTables(members, booked == null ? List.of() : booked, bookedSize);
        return tables.stream().map(t -> {
            TableDto dto = new TableDto();
            dto.setId(t.getId());
            dto.setName(t.getName());
            dto.setCapacity(t.getCapacity());
            return dto;
        }).toList();
    }
}
