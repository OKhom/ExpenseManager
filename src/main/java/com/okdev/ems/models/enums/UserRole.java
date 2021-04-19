package com.okdev.ems.models.enums;

public enum UserRole {
    ADMIN,
    USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
