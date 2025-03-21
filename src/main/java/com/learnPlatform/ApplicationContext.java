package com.learnPlatform;

import com.learnPlatform.controller.*;
import com.learnPlatform.dao.UserDao;
import com.learnPlatform.dao.impl.UserDaoImpl;
import com.learnPlatform.in.ConsoleInput;
import com.learnPlatform.model.entity.User;
import com.learnPlatform.out.ConsoleOutputData;
import com.learnPlatform.service.*;
import com.learnPlatform.service.impl.*;
import com.learnPlatform.util.Config;
import com.learnPlatform.util.ConfigLoader;
import com.learnPlatform.util.DatabaseManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static final Map<String, Object> CONTEXT = new HashMap<>();

    public static void loadContext() {
        loadDAOLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayers();
    }

    public static void loadAuthorizedUser(User user) {
        CONTEXT.put("authorize", user);
    }

    public static void cleanAuthorizedUser() {
        CONTEXT.remove("authorize");
    }

    public static User getAuthorizeUser() {
        return (User) CONTEXT.get("authorize");
    }

    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadInputOutputLayers() {
        CONTEXT.put("input", new ConsoleInput());
        CONTEXT.put("output", new ConsoleOutputData());
    }

    private static void loadControllers() {
        AuthController authController = new AuthController((SecurityService) CONTEXT.get("securityService"));
        CONTEXT.put("authController", authController);

        NotificationController notificationController = new NotificationController((NotificationService)
                CONTEXT.get("notificationService"));
        CONTEXT.put("notificationController", notificationController);

        UserController userController = new UserController((UserService) CONTEXT.get("userService"));
        CONTEXT.put("userController", userController);
    }

    private static void loadServiceLayer() {
        CONTEXT.put("securityService", new SecurityServiceImpl((UserDao) CONTEXT.get("userDao")));
        CONTEXT.put("userService", new UserServiceImpl((UserDao) CONTEXT.get("userDao")));
    }

    private static void loadDAOLayer() {
        Config config = ConfigLoader.loadConfig("config.yaml");
        DatabaseManager databaseManager = new DatabaseManager(config.database);
        DataSource dataSource = databaseManager.getDataSource();

        CONTEXT.put("userDao", new UserDaoImpl(dataSource));
    }
}
