package com.learnPlatform.handler;

import com.learnPlatform.ApplicationContext;
import com.learnPlatform.controller.AuthController;
import com.learnPlatform.in.Input;
import com.learnPlatform.model.entity.User;
import com.learnPlatform.model.type.UserLocation;
import com.learnPlatform.out.OutputData;
import com.learnPlatform.wrapper.SecurityWrapper;

import static com.learnPlatform.model.type.UserRole.ADMIN;

public class SecurityHandler {

    public static UserLocation handleRegistration(Input inputData, OutputData outputData, AuthController controller) {
        SecurityWrapper swRegister = askCredentials(inputData, outputData);
        User registeredUser = controller.register(swRegister.getLogin(), swRegister.getPassword());
        ApplicationContext.loadAuthorizedUser(registeredUser);

        return UserLocation.MAIN_MENU_LOC;
    }

    public static UserLocation handleAuthorization(Input inputData, OutputData outputData, AuthController controller) {
        SecurityWrapper swAuthorize = askCredentials(inputData, outputData);
        User authorizedUser = controller.authorize(swAuthorize.getLogin(), swAuthorize.getPassword());

        if (authorizedUser != null) {
            ApplicationContext.loadAuthorizedUser(authorizedUser);
            return isAdmin(authorizedUser) ? UserLocation.ADMIN_MENU_LOC : UserLocation.MAIN_MENU_LOC;
        }

        return UserLocation.MAIN_MENU_LOC;
    }

    /**
     * Prompt user for login credentials.
     *
     * @param inputData  Input source for reading user input.
     * @param outputData Output destination for displaying messages.
     * @return SecurityWrapper containing user-provided login and password.
     */
    private static SecurityWrapper askCredentials(Input inputData, OutputData outputData) {
        final String loginMsg = "Введите логин:";
        outputData.output(loginMsg);
        String login = inputData.in().toString();

        final String passMsg = "Введите пароль:";
        outputData.output(passMsg);
        String password = inputData.in().toString();

        return SecurityWrapper.builder()
                .login(login)
                .password(password)
                .build();
    }

    public static boolean isAdmin(User user) {
        return user.getUserRole().equals(ADMIN);
    }
}
