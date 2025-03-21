package com.financeTracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
