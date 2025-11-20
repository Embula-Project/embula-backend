package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@Getter
@Setter
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private com.embula.embula_backend.entity.RestaurantTable table;

    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;
    private int members;
    private String mealType;
    private String customerName;
    private String customerPhone;
    private String status; // e.g. CONFIRMED, CANCELLED

    @Version
    private Long version;

    // Constructors, getters, setters
    public Reservation() {}
    // getters / setters omitted for brevity — add them or use Lombok
    // ... (include all standard getters/setters)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RestaurantTable getTable() { return table; }
    public void setTable(RestaurantTable table) { this.table = table; }
    public LocalDateTime getReservationStart() { return reservationStart; }
    public void setReservationStart(LocalDateTime reservationStart) { this.reservationStart = reservationStart; }
    public LocalDateTime getReservationEnd() { return reservationEnd; }
    public void setReservationEnd(LocalDateTime reservationEnd) { this.reservationEnd = reservationEnd; }
    public int getMembers() { return members; }
    public void setMembers(int members) { this.members = members; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
