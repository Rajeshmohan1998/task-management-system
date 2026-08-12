package com.taskapp.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException , IOException{
        String authHeader= request.getHeader("Authorization");

        //Piece 1: no token at all -> pass through unatuthenticated
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        //Piewce 2:Strip "Bearer " to get the raw token
        String token = authHeader.substring(7);

        //Piece 3:extract username, validate
        String username = jwtUtil.extractUsername(token);

        if(username!= null && SecurityContextHolder.getContext().getAuthentication() == null){
            boolean isValid = jwtUtil.validateToken(token, username);

            if(isValid){
                String role = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_"+role))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //Piece 4:always continue the chain
        filterChain.doFilter(request, response);
    }
}
