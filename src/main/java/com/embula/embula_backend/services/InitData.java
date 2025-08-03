package com.embula.embula_backend.services;

import com.embula.embula_backend.entity.Customer;
import com.embula.embula_backend.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner runner(CustomerRepository repo) {
        return args -> {
            Customer customer = new Customer();
            customer.setName("Dewmin");
            customer.setEmail("dewmin@example.com");
            customer.setPhone("0771234567");

            repo.save(customer); // 🔥 This creates the document & collection if it doesn't exist
        };
    }
}
