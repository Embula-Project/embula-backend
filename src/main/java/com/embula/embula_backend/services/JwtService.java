package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.LoginRequest;
import com.embula.embula_backend.dto.response.LoginResponse;

public interface JwtService {

    public LoginResponse createJwtToken(LoginRequest loginRequest);
    public LoginResponse refreshAccessToken(String refreshToken) throws Exception;
}
