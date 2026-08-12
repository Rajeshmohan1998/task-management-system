package com.taskapp.auth.service;

import com.taskapp.auth.dto.LoginRequest;
import com.taskapp.auth.dto.LoginResponse;
import com.taskapp.auth.dto.RefreshRequest;
import com.taskapp.auth.entity.User;
import com.taskapp.auth.repository.UserRepository;
import com.taskapp.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("User Not found"+request.getUsername()));

        String accessToken = jwtUtil.generateAccessToken(user.getUserName(),user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserName());

        return new LoginResponse(accessToken,refreshToken,user.getUserName(), user.getRole().name());

    }

    public LoginResponse refreshAccessToken(RefreshRequest request){

        String refreshToken = request.getRefreshToken();
        String username = jwtUtil.extractUsername(refreshToken);

        if(username == null || !jwtUtil.validateToken(refreshToken,username)){
            throw new BadCredentialsException("Invalid or expired refresh token");

        }
        User user= userRepository.findByUserName(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found:" + username));

        String newAccessToken = jwtUtil.generateAccessToken(user.getUserName(),user.getRole().name());
        return new LoginResponse(newAccessToken,refreshToken, user.getUserName(),user.getRole().name());

    }


}
