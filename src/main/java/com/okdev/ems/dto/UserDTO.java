package com.okdev.ems.dto;

import com.okdev.ems.models.enums.UserRole;

public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    public UserDTO() {
    }

    public UserDTO(Long userId, String firstName, String lastName, String email, UserRole role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public static UserDTO of(Long userId, String firstName, String lastName, String email, UserRole role) {
        return new UserDTO(userId, firstName, lastName, email, role);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
