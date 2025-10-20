package com.embula.embula_backend.config;

import com.embula.embula_backend.services.JwtService;
import com.embula.embula_backend.services.UserService;
import com.embula.embula_backend.services.impl.CustomerDetailsService;
import com.embula.embula_backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken= null;
        System.out.println("requestTokenHeader:"+requestTokenHeader);

        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")){
            jwtToken = requestTokenHeader.substring(7);
            System.out.println("jwtToken:"+jwtToken);
            try{
                username = jwtUtil.getUsernameFromToken(jwtToken);
                System.out.println("username:"+username);
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }catch(ExpiredJwtException e){
                System.out.println(e.getMessage());
            }
        }else{
            System.out.println("Jwt is not validated");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails =customerDetailsService.loadUserByUsername(username);

            if(jwtUtil.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                System.out.println("Authorities for " + username + ": " + userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }


}
