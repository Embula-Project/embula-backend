package com.embula.embula_backend.advisor;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled =true
)
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/v1/order/cancelOrder", "/api/v1/order/saveOrder", "/api/v1/order/viewAllOrders").authenticated()
                        .requestMatchers("/api/v1/payments/savePayment").authenticated()

                        .anyRequest().permitAll()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
