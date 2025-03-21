package com.learnPlatform;

import com.learnPlatform.controller.*;
import com.learnPlatform.exceptions.authorization.AuthorizationException;
import com.learnPlatform.exceptions.registration.RegistrationException;
import com.learnPlatform.exceptions.validation.NotValidArgumentException;
import com.learnPlatform.handler.*;
import com.learnPlatform.in.Input;
import com.learnPlatform.model.type.UserLocation;
import com.learnPlatform.out.OutputData;
import com.learnPlatform.util.Config;
import com.learnPlatform.util.ConfigLoader;
import com.learnPlatform.util.DatabaseManager;
import com.learnPlatform.util.LiquibaseMigrator;

import static com.learnPlatform.handler.SecurityHandler.handleAuthorization;
import static com.learnPlatform.handler.SecurityHandler.handleRegistration;
import static com.learnPlatform.model.type.UserLocation.*;
import static com.learnPlatform.constants.ConsoleConstants.*;
import static com.learnPlatform.constants.ConsoleConstants.ADMIN_MENU;
import static com.learnPlatform.constants.ConsoleConstants.MAIN_MENU;


/**
 * The ApplicationRunner class is responsible for running the main loop of the application.
 * It handles user input, manages different user locations, and delegates tasks to appropriate handlers.
 */
public class ApplicationRunner {

    private static AuthController authController;
    private static UserLocation userLocation;
    private static UserController userController;

    public static void run() {
        Config config = ConfigLoader.loadConfig("config.yaml");
        DatabaseManager databaseManager = new DatabaseManager(config.database);
        LiquibaseMigrator liquibaseMigrator = new LiquibaseMigrator(databaseManager, config.liquibase.changelog);
        liquibaseMigrator.executeMigrations();

        ApplicationContext.loadContext();
        Input inputData = (Input) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        authController = (AuthController) ApplicationContext.getBean("authController");
        userController = (UserController) ApplicationContext.getBean("userController");

        userLocation = SECURITY_LOC;

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userLocation) {
                    case SECURITY_LOC -> handleSecurity(inputData, outputData);
                    case MAIN_MENU_LOC -> handleMainMenu(inputData, outputData);
                    case ADMIN_MENU_LOC -> handleAdminMenu(inputData, outputData);
                    case USER_SETTINGS_LOC -> handleUserSettings(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizationException |
                     NotValidArgumentException |
                     RegistrationException e) {
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                outputData.errOutput("Unknown error. More details: " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeIn();
    }

    /**
     * Handles the main menu interactions, where the user can navigate to other menu sections.
     *
     * @param inputData  The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleMainMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(MAIN_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = USER_SETTINGS_LOC;
                    return;
                }
                case "2" -> {
                    userLocation = ADMIN_MENU_LOC;
                    return;
                }
                case "3" -> {
                    userLocation = SECURITY_LOC;
                    return;
                }
                case "0" -> {
                    userLocation = EXIT;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }


    /**
     * Handles security menu interactions, where users can choose to register, authorize, or exit.
     *
     * @param inputData  The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    public static void handleSecurity(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(SECURITY_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = handleRegistration(inputData, outputData, authController);
                    return;
                }
                case "2" -> {
                    userLocation = handleAuthorization(inputData, outputData, authController);
                    return;
                }
                case "3" -> {
                    userLocation = EXIT;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    /**
     * Handles the user menu interactions, allowing the user to edit profile or delete it.
     *
     * @param inputData  The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleUserSettings(Input inputData, OutputData outputData) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();
        while (true) {
            outputData.output(USER_SETTINGS_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = UserSettingsHandler.editProfileName(inputData, outputData, userController, userId);
                    return;
                }
                case "2" -> {
                    userLocation = UserSettingsHandler.resetPasswordAndEmail(inputData, outputData, userController, userId);
                    return;
                }
                case "3" -> {
                    userLocation = UserSettingsHandler.deleteProfile(inputData, outputData, userController, userId);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }


    /**
     * Handles the admin menu interactions, allowing the admin to perform various operations such as viewing users,
     * managing transactions, and blocking/unblocking users.
     *
     * @param inputData  The input interface to handle user input.
     * @param outputData The output interface to display information to the user.
     */
    private static void handleAdminMenu(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(ADMIN_MENU);
            Object input = inputData.in();
            switch (input.toString()) {
                case "1" -> {
                    userLocation = AdminHandler.handleShowListOfRegisteredUsers(outputData, userController);
                    return;
                }
                case "2" -> {
                    userLocation = AdminHandler.handleBlockUser(inputData, outputData, userController);
                    return;
                }
                case "3" -> {
                    userLocation = AdminHandler.handleUnblockUser(inputData, outputData, userController);
                    return;
                }
                case "0" -> {
                    userLocation = MAIN_MENU_LOC;
                    return;
                }
                default -> outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void exitProcess(OutputData outputData) {
        outputData.output("До свидания!");
        ApplicationContext.cleanAuthorizedUser();
    }
}

