package com.example.spacemate.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    USER,
    OWNER,
    ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return USER;
        }
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        return USER;
    }
}