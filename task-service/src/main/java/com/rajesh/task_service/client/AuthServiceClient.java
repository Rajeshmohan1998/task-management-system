package com.rajesh.task_service.client;

import com.rajesh.task_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="auth-service", url = "${auth-service.url}")
public interface AuthServiceClient {

    @GetMapping("/api/users/username/{username}")
    UserResponse getUserDetails(@PathVariable("username") String username);
}
