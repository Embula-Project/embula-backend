package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.AdminDTO;
import com.embula.embula_backend.dto.CustomerDTO;
import com.embula.embula_backend.dto.UserDTO;
import com.embula.embula_backend.dto.response.ViewCustomerDTO;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.AdminService;
import com.embula.embula_backend.services.CustomerService;
import com.embula.embula_backend.services.UserService;
import com.embula.embula_backend.util.StandardResponse;
import com.embula.embula_backend.util.mappers.UserMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


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

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getAllCustomers(){
        try{
            List<ViewCustomerDTO> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(
                    new StandardResponse(200,"Success", customers),
                    HttpStatus.OK
            );
        }catch(Exception e){
            return new ResponseEntity<>(
                    new StandardResponse(500,"Error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

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
                    customerDTO.setStatus(com.embula.embula_backend.entity.enums.CustomerStatus.ACTIVE); // Set status to ACTIVE enum
                    customerDTO.setCreatedAt(LocalDateTime.now());

                    // Save customer
                    customerService.saveCustomer(customerDTO);
                }

                // If the user role is ADMIN, also create an admin record
                if(userDTO.getRole() != null && userDTO.getRole().equalsIgnoreCase("ADMIN")) {
                    AdminDTO adminDTO = new AdminDTO();
                    adminDTO.setId(String.valueOf(savedUser.getId())); // Same ID as User
                    adminDTO.setAdminName(userDTO.getAdminName());
                    adminDTO.setAdminNumber(userDTO.getAdminNumber());

                    // Save admin
                    adminService.saveAdmin(adminDTO);
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

    @PatchMapping("/setStatus/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> setUserInactive(@PathVariable String userId){
        try {
            String message = userService.setUserInactive(userId);
            if(message.contains("updated to Inactive")) {
                return new ResponseEntity<>(
                        new StandardResponse(200, "Success", message),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        new StandardResponse(404, "Not Found", message),
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(500, "Error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
