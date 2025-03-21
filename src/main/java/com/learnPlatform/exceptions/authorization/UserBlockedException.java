package com.learnPlatform.exceptions.authorization;

import com.learnPlatform.constants.ErrorMessages;

public class UserBlockedException extends AuthorizationException {
    public UserBlockedException() {
        super(ErrorMessages.USER_BLOCKED);
    }
}