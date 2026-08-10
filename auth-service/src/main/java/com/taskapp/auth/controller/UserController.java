package com.taskapp.auth.controller;

import com.taskapp.auth.dto.RegisterRequest;
import com.taskapp.auth.dto.UserResponse;
import com.taskapp.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUserName(@PathVariable String username){
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }
}
