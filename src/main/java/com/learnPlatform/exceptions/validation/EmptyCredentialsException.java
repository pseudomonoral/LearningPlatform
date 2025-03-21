package com.learnPlatform.exceptions.validation;

import com.learnPlatform.constants.ErrorMessages;
import com.learnPlatform.exceptions.registration.RegistrationException;

public class EmptyCredentialsException extends RegistrationException {
    public EmptyCredentialsException() {
        super(ErrorMessages.EMPTY_CREDENTIALS);
    }
}