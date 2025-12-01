package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.LoginRequest;
import com.embula.embula_backend.dto.response.LoginResponse;
import com.embula.embula_backend.entity.User;
import com.embula.embula_backend.entity.enums.UserRole;
import com.embula.embula_backend.repository.UserRepository;
import com.embula.embula_backend.services.JwtService;
import com.embula.embula_backend.util.JwtUtil;
import com.embula.embula_backend.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.token-validity}")
    private int ACCESS_TOKEN_VALIDITY;

    @Value("${jwt.refresh-token-validity}")
    private int REFRESH_TOKEN_VALIDITY;

    @PostMapping("/authentication")
    @Operation(summary = "Login", description = "Authenticate user and receive access and refresh tokens as HTTP-only cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<?> createJwtTokenAndLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = jwtService.createJwtToken(loginRequest);

        // Set access token as HTTP-only cookie
        Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Set to true in production with HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(ACCESS_TOKEN_VALIDITY / 1000); // Convert milliseconds to seconds
        response.addCookie(accessTokenCookie);

        // Set refresh token as HTTP-only cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Set to true in production with HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(REFRESH_TOKEN_VALIDITY / 1000); // Convert milliseconds to seconds
        response.addCookie(refreshTokenCookie);

        // Return only user data (without tokens)
        return ResponseEntity.ok(new StandardResponse(200, "Login successful", loginResponse.getUser()));
    }

    @PostMapping("/refresh-token")
    @Operation(
        summary = "Refresh access token",
        description = "Refreshes the access token using the refresh token from HTTP-only cookie. No request body needed - the refresh token is automatically sent via Cookie header."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<?> refreshToken(
            @Parameter(hidden = true) // Hide from Swagger UI - it's in cookie, not body
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StandardResponse(401, "Refresh token not found", null));
        }

        try {
            LoginResponse loginResponse = jwtService.refreshAccessToken(refreshToken);

            // Set NEW access token as HTTP-only cookie (because old one expired)
            Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(false); // Set to true in production with HTTPS
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(ACCESS_TOKEN_VALIDITY / 1000);
            response.addCookie(accessTokenCookie);

            // Send back the SAME refresh token cookie (it's still valid, not expired)
            Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false); // Set to true in production with HTTPS
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(REFRESH_TOKEN_VALIDITY / 1000);
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new StandardResponse(200, "Token refreshed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StandardResponse(401, "Invalid refresh token", null));
        }
    }

    @GetMapping("/auth/me")
    @Operation(
        summary = "Get current user",
        description = "Returns current user information based on the access token from HTTP-only cookie. No request needed - the access token is automatically sent via Cookie header."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated or invalid token")
    })
    public ResponseEntity<?> getCurrentUser(
            @Parameter(hidden = true) // Hide from Swagger UI
            HttpServletRequest request) {
        try {
            // Get authentication from SecurityContext (set by JwtRequestFilter)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new StandardResponse(401, "Not authenticated", null));
            }

            // Extract JWT token from cookie
            String jwtToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (jwtToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new StandardResponse(401, "Access token not found", null));
            }

            // Extract user info directly from token - NO DATABASE CALL
            String email = jwtUtil.getUsernameFromToken(jwtToken);
            String firstName = jwtUtil.getFirstNameFromToken(jwtToken);
            String lastName = jwtUtil.getLastNameFromToken(jwtToken);
            String roleString = jwtUtil.getRoleFromToken(jwtToken);
            UserRole role = UserRole.valueOf(roleString);

            // Create user object from token data
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(role);
            // Password is null - we don't store it in token

            return ResponseEntity.ok(new StandardResponse(200, "User retrieved successfully", user));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StandardResponse(401, "Invalid or expired token", null));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Clears authentication cookies (access token and refresh token)")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear access token cookie
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Set to true in production with HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        // Clear refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Set to true in production with HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new StandardResponse(200, "Logout successful", null));
    }
}
