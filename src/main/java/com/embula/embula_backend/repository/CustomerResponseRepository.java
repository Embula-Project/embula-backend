package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.CustomerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerResponseRepository extends JpaRepository<CustomerResponse, String> {
}
