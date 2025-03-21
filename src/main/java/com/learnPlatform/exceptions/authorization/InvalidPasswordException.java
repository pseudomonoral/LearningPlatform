package com.learnPlatform.exceptions.authorization;

import com.learnPlatform.constants.ErrorMessages;

public class InvalidPasswordException extends AuthorizationException {
    public InvalidPasswordException() {
        super(ErrorMessages.INVALID_PASSWORD);
    }
}