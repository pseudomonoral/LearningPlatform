package com.learnPlatform.exceptions.registration;

import com.learnPlatform.constants.ErrorMessages;

public class UserAlreadyExistsException extends RegistrationException {
    public UserAlreadyExistsException() {
        super(ErrorMessages.USER_ALREADY_EXISTS);
    }
}