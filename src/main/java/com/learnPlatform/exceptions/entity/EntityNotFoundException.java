package com.learnPlatform.exceptions.entity;

import com.learnPlatform.model.type.EntityName;

public class EntityNotFoundException extends RuntimeException {
    private final static String EXCEPTION_MESSAGE = "Entity %s with id %s not found";

    public EntityNotFoundException(EntityName entityType, Long id) {
        super(EXCEPTION_MESSAGE.formatted(entityType.getValue(), id));
    }
}
