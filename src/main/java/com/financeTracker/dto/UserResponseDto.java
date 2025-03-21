package com.financeTracker.dto;

import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String login;
    private String name;
    private UserRole userRole;
    private UserStatus userStatus;
}