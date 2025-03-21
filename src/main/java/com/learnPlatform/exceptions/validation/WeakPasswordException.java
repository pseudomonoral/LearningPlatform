package com.learnPlatform.exceptions.validation;

import com.learnPlatform.constants.ErrorMessages;
import com.learnPlatform.exceptions.registration.RegistrationException;

public class WeakPasswordException extends RegistrationException {
  public WeakPasswordException() {
    super(ErrorMessages.WEAK_PASSWORD);
  }
}