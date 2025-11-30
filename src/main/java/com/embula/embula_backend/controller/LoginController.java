package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.UserDTO;
import com.embula.embula_backend.dto.request.LoginRequest;
import com.embula.embula_backend.dto.request.RefreshTokenRequest;
import com.embula.embula_backend.dto.response.LoginResponse;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.JwtService;
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
@RequestMapping("/api/v1/login")
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authentication")
    public LoginResponse createJwtTokenAndLogin(@RequestBody LoginRequest loginRequet){
        System.out.println(loginRequet);
        LoginResponse loginResponse= new LoginResponse();
        loginResponse = jwtService.createJwtToken(loginRequet);

        return loginResponse;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        LoginResponse response;
        try{
            response = jwtService.refreshAccessToken(refreshToken);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }


}
