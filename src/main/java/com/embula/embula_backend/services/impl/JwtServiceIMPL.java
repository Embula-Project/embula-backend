package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.LoginRequest;
import com.embula.embula_backend.dto.response.LoginResponse;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.JwtService;
import com.embula.embula_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtServiceIMPL implements JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponse createJwtToken(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        User user = userRepository.findById(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                )
        );

        return new LoginResponse(user, token);
    }
}
