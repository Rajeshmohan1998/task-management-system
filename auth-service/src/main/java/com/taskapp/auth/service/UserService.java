package com.taskapp.auth.service;

import com.taskapp.auth.dto.RegisterRequest;
import com.taskapp.auth.dto.UserResponse;
import com.taskapp.auth.entity.Role;
import com.taskapp.auth.entity.User;
import com.taskapp.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder= new BCryptPasswordEncoder();
    }

    public UserResponse register(RegisterRequest request){
        log.info("Register attempt for username : {}", request.getUserName());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(hashedPassword);
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id: {}", savedUser.getId());

        return mapToUserResponse(user);

    }

    public UserResponse getUserByUsername(String username){
        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new RuntimeException("User Not found:" +username));
        return mapToUserResponse(user);

    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getUserName());
        response.setRole(user.getRole());
        return response;
    }


}
