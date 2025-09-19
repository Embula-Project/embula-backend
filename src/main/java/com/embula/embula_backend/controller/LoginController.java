package com.embula.embula_backend.controller;


import com.embula.embula_backend.dto.UserDTO;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.UserService;
import com.embula.embula_backend.util.StandardResponse;
import com.embula.embula_backend.util.mappers.UserMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user-login")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMappers userMapper;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/saveUser")
    public ResponseEntity<StandardResponse> saveUser(@RequestBody UserDTO userDTO){
        ResponseEntity<StandardResponse> responseEntity = null;
        try{
            String hashPassowrd= passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(hashPassowrd);
            userDTO.setRole("ROLE_"+userDTO.getRole());
            User user = userMapper.userDTOToUser(userDTO);
            User savedUser = userRepository.save(user);
            if(savedUser!=null){
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
}
