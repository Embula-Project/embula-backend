package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.dto.UserDTO;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.CustomerService;
import com.embula.embula_backend.util.StandardResponse;
import com.embula.embula_backend.util.mappers.UserMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMappers userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerService customerService;


    @PostMapping("/register-user")
    public ResponseEntity<StandardResponse> saveUser(@RequestBody UserDTO userDTO){
        ResponseEntity<StandardResponse> responseEntity = null;
        try{
            String hashPassowrd= passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(hashPassowrd);
            userDTO.setRole(userDTO.getRole());
            User user = userMapper.userDTOToUser(userDTO);
            User savedUser = userRepository.save(user);

            if(savedUser!=null){
                // If the user role is CUSTOMER, also create a customer record
                if(userDTO.getRole() != null && userDTO.getRole().equalsIgnoreCase("CUSTOMER")) {
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO.setId(String.valueOf(savedUser.getId())); // Same ID as User
                    customerDTO.setEmail(savedUser.getEmail()); // Same email as User
                    customerDTO.setFirstName(userDTO.getFirstName());
                    customerDTO.setLastName(userDTO.getLastName());
                    customerDTO.setAddress(userDTO.getAddress());
                    customerDTO.setPhone(userDTO.getPhone());
                    customerDTO.setImage(userDTO.getImage());
                    customerDTO.setStatus("Active"); // Set status to Active
                    customerDTO.setCreatedAt(LocalDateTime.now());

                    // Save customer
                    customerService.saveCustomer(customerDTO);
                }

                responseEntity= new ResponseEntity<>(
                        new StandardResponse(200,"Created", savedUser ),
                        HttpStatus.CREATED
                );
            }
        }catch(Exception e){
            responseEntity = new ResponseEntity<>(
                    new StandardResponse(500,"Error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return responseEntity;

    }

    @GetMapping("/for-customer")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public String forCustomer(){
        return "This is Only for Customers";
    }

    @GetMapping("/for-admins")
    @PreAuthorize("hasRole('ADMIN')")
    public String forAdmins(){
        return "This is Only for Admin";
    }

}
