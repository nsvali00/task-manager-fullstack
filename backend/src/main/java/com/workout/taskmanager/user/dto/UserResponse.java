package com.workout.taskmanager.user.dto;

import com.workout.taskmanager.common.enums.Role;

public class UserResponse {

    private String email;
    private String firstName;
    private String lastName;
    private Role role;

    public UserResponse(String email, String firstName, String lastName, Role role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Role getRole() { return role; }
}
