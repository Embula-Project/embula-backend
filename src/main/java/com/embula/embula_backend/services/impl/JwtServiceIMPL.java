//package com.embula.embula_backend.services.impl;
//
//import com.embula.embula_backend.dto.request.LoginRequest;
//import com.embula.embula_backend.dto.response.LoginResponse;
//import com.embula.embula_backend.entity.User;
//import com.embula.embula_backend.repository.UserRepository;
//import com.embula.embula_backend.services.JwtService;
//import com.embula.embula_backend.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class JwtServiceIMPL implements JwtService {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtUtil jwtUtil;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public LoginResponse createJwtToken(LoginRequest loginRequest){
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//        );
//
//        User user = userRepository.findById(loginRequest.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        String token = jwtUtil.generateToken(
//                new org.springframework.security.core.userdetails.User(
//                        user.getEmail(),
//                        user.getPassword(),
//                        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
//                )
//        );
//
//        return new LoginResponse(user, token);
//    }
//}


package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.LoginRequest;
import com.embula.embula_backend.dto.response.LoginResponse;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.JwtService;
import com.embula.embula_backend.util.JwtUtil;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class JwtServiceIMPL implements JwtService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtServiceIMPL(UserRepository userRepo, JwtUtil jwtUtil, @Lazy AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username).get();

        if(user!=null){
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),//get the username
                    user.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))//get the password to compare -> this is getting from the database

            );
        }else{
            throw new UsernameNotFoundException("User Not Found with the given Username");
        }
    }


    public String getAuthority(User user){//an User Can have more than one role
        String userRole = user.getRole().toString();

        return userRole;
    }

    public void authenticate(String userName, String userPassword) throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        }catch(BadCredentialsException e){
            throw new Exception("Invalid Credentials", e);
        }
    }

    @Override
    public LoginResponse createJwtToken(LoginRequest loginRequest) {
        String userName = loginRequest.getUsername();
        String userPassword = loginRequest.getPassword();

        try{
            authenticate(userName, userPassword);
        }catch(Exception e){
            throw new UsernameNotFoundException("Invalid username or password");
        }


        UserDetails userDetails =loadUserByUsername(userName);
        User user = userRepo.findById(userName).get();
        System.out.println(user);
        String accessToken = jwtUtil.generateToken(
                userDetails,
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        LoginResponse loginResponse = new LoginResponse(
                user,
                accessToken,
                refreshToken
        );

        return loginResponse;
    }

    //Validate the Refresh Token
    public LoginResponse refreshAccessToken(String refreshToken) throws Exception{
        if(refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)){
            throw new Exception("Invalid Refresh Token");
        }

        // Extract username from refresh token - NO DATABASE CALL
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        // Create minimal UserDetails for token generation
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "", // Password not needed for token generation
                Collections.emptyList()
        );

        // Note: Refresh token only contains email, not firstName/lastName/role
        // We need to fetch user data once to create new access token with full claims
        User user = userRepo.findById(username).orElseThrow(() -> new Exception("User not found"));

        String newAccessToken = jwtUtil.generateToken(
                userDetails,
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );

        // Return same refresh token (it's still valid)
        return new LoginResponse(user, newAccessToken, refreshToken);
    }
}
