package com.workout.taskmanager.user.dto;

import com.workout.taskmanager.common.enums.Role;

public record UserResponse(String email, String firstName, String lastName, Role role) {
}
