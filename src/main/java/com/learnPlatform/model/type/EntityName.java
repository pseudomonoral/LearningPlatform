package com.learnPlatform.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityName {
    USER("User");

    private final String value;
}

