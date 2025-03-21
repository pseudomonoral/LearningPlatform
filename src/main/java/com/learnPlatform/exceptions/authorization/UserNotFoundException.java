package com.learnPlatform.exceptions.authorization;

import com.learnPlatform.constants.ErrorMessages;

public class UserNotFoundException extends AuthorizationException {
    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }
}
