package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.table.id = :tableId "
            + "AND r.status = 'CONFIRMED' "
            + "AND (r.reservationStart < :end AND r.reservationEnd > :start)")
    List<Reservation> findConflictsForTable(@Param("tableId") Long tableId,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    @Query("SELECT r.table.id FROM Reservation r WHERE r.status = 'CONFIRMED' "
            + "AND (r.reservationStart < :end AND r.reservationEnd > :start)")
    List<Long> findReservedTableIds(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);
}
