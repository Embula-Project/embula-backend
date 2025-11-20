package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Check conflicts for a specific table within a time range
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.table.id = :tableId " +
            "AND r.status <> 'CANCELLED' " +
            "AND (r.reservationStart < :end AND r.reservationEnd > :start)")
    List<Reservation> findConflictsForTable(@Param("tableId") Long tableId,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    // When updating a reservation, exclude the same reservation ID
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.table.id = :tableId " +
            "AND r.id <> :reservationId " +
            "AND r.status <> 'CANCELLED' " +
            "AND (r.reservationStart < :end AND r.reservationEnd > :start)")
    List<Reservation> findConflictsForTableExcludingReservation(@Param("tableId") Long tableId,
                                                                @Param("start") LocalDateTime start,
                                                                @Param("end") LocalDateTime end,
                                                                @Param("reservationId") Long reservationId);

    // Find all reserved table IDs during a time interval
    @Query("SELECT DISTINCT r.table.id FROM Reservation r " +
            "WHERE r.status <> 'CANCELLED' " +
            "AND (r.reservationStart < :end AND r.reservationEnd > :start)")
    List<Long> findReservedTableIds(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    // Same as above but excluding a specific reservation
    @Query("SELECT DISTINCT r.table.id FROM Reservation r " +
            "WHERE r.id <> :reservationId " +
            "AND r.status <> 'CANCELLED' " +
            "AND (r.reservationStart < :end AND r.reservationEnd > :start)")
    List<Long> findReservedTableIdsExcludingReservation(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end,
                                                        @Param("reservationId") Long reservationId);
}
