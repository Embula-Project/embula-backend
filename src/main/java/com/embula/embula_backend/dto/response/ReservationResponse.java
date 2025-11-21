package com.embula.embula_backend.dto.response;

import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    private Long tableId;
    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;
    private String status;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) { this.tableId = tableId; }
    public LocalDateTime getReservationStart() { return reservationStart; }
    public void setReservationStart(LocalDateTime reservationStart) { this.reservationStart = reservationStart; }
    public LocalDateTime getReservationEnd() { return reservationEnd; }
    public void setReservationEnd(LocalDateTime reservationEnd) { this.reservationEnd = reservationEnd; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
