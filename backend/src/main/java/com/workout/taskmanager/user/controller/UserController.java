package com.workout.taskmanager.user.controller;

import com.workout.taskmanager.user.dto.UserResponse;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return new UserResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}
