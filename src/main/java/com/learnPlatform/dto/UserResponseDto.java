package com.learnPlatform.dto;

import com.learnPlatform.model.type.UserRole;
import com.learnPlatform.model.type.UserStatus;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String login;
    private String name;
    private UserRole userRole;
    private UserStatus userStatus;
}