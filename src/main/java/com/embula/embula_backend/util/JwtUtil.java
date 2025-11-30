package com.embula.embula_backend.util;

import com.embula.embula_backend.entity.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.token-validity}")
    private int TOKEN_VALIDITY;

    @Value("${jwt.refresh-token-validity}")
    private int REFRESH_TOKEN_VALIDITY;

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateRefreshToken( String token){
        try{
            Claims claims = getAllClaimsFromToken(token);
            boolean isRefresh = "refresh".equals(claims.get("type"));
            return isRefresh && !isTokenExpired(token);
        }catch(Exception e){
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        final Date expiration= getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }


    public String generateToken(UserDetails userDetails , String firstName , String lastName , UserRole role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken (UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_VALIDITY *100))
                .claim("type", "refresh")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

}