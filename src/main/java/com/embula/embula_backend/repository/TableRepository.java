package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.RestaurantTable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {

    @Query("SELECT t FROM RestaurantTable t WHERE t.capacity >= :members "
            + "AND t.id NOT IN :bookedIds AND t.isActive = true "
            + "ORDER BY t.capacity ASC")
    List<RestaurantTable> findAvailableTables(
            @Param("members") int members,
            @Param("bookedIds") List<Long> bookedIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM RestaurantTable t WHERE t.id = :id")
    Optional<RestaurantTable> findByIdForUpdate(@Param("id") Long id);
}
