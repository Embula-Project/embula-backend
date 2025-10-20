package com.embula.embula_backend.repository;

import com.embula.embula_backend.dto.queryInterface.StatusCustomerOrderInterface;
import com.embula.embula_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(
            value = "SELECT c.first_name AS firstName, c.last_name AS lastName, c.phone AS phone, c.email AS email, " +
                    "o.order_name AS orderName, o.order_created_date AS orderCreatedDate " +
                    "FROM customer c, orders o " +
                    "WHERE o.customer_id = c.customer_id AND c.status = :status",
            nativeQuery = true
    )
    List<StatusCustomerOrderInterface> statusCustomerOrders(@Param("status") String status, Pageable pageable);

    @Query(
            value = "SELECT count(*) " +
                    "FROM customer c, orders o " +
                    "WHERE o.customer_id = c.customer_id AND c.status = :status",
            nativeQuery = true
    )
    Long countStatusCustomerOrders(@Param("status") String status);


}
