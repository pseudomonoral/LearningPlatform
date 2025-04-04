package com.learnPlatform.exceptions.entity;

import com.learnPlatform.model.type.EntityName;

public class EntityAlreadyExistException extends RuntimeException {

    private static final String ENTITY_ALREADY_EXIST_MESSAGE= "Entity %s with userId %d already exists";

    public EntityAlreadyExistException(EntityName entityName, Long userId) {
        super(ENTITY_ALREADY_EXIST_MESSAGE.formatted(entityName, userId));
    }
}
