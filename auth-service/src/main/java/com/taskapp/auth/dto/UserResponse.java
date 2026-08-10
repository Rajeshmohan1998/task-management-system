package com.taskapp.auth.dto;

import com.taskapp.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String userName;

    private String email;

    private Role role;
}
