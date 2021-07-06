package com.okdev.ems.dto;

import com.okdev.ems.models.enums.UserRole;

public class UserDTO {
    private Long userId;
    private Long currencyId;
    private String firstName;
    private String lastName;
    private String email;
    private String currencyName;
    private String currencyShortName;
    private UserRole role;

    public UserDTO() {
    }

    public UserDTO(Long userId, Long currencyId, String firstName, String lastName, String email, String currencyName, String currencyShortName, UserRole role) {
        this.userId = userId;
        this.currencyId = currencyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.currencyName = currencyName;
        this.currencyShortName = currencyShortName;
        this.role = role;
    }

    public static UserDTO of(Long userId, Long currencyId, String firstName, String lastName, String email, String currencyName, String currencyShortName, UserRole role) {
        return new UserDTO(userId, currencyId, firstName, lastName, email, currencyName, currencyShortName, role);
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

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyShortName() {
        return currencyShortName;
    }

    public void setCurrencyShortName(String currencyShortName) {
        this.currencyShortName = currencyShortName;
    }
}
