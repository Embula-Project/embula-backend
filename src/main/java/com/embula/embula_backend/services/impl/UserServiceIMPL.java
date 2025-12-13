package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.entity.Customer;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.entity.enums.CustomerStatus;
import com.embula.embula_backend.repository.CustomerRepository;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public String setUserInactive(String userId){
        try {
            Long userIdLong = Long.parseLong(userId);
            User user = userRepository.findById(userIdLong).orElse(null);
            if(user == null){
                return "User not found with Id: " + userId;
            } else if (user.getRole().name().equals("CUSTOMER")){
                Customer customer = customerRepository.findByEmail(user.getEmail()).orElse(null);
                if(customer == null){
                    return "Customer not found with email: " + user.getEmail();
                }
                customer.setStatus(CustomerStatus.INACTIVE);
                customerRepository.save(customer);
                return "Customer status updated to Inactive for userId: " + userId;
            } else {
                return "User is not a customer, role: " + user.getRole().name();
            }
        } catch (NumberFormatException e) {
            return "Invalid userId format: " + userId;
        }
    }
}
