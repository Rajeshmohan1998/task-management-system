package com.taskapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class LoginResponse {

    String accessToken;
    String refreshToken;
    String username;
    String role;
}
