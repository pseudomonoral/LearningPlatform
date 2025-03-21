package com.financeTracker;

import com.financeTracker.controller.*;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.in.ConsoleInput;
import com.financeTracker.model.entity.User;
import com.financeTracker.out.ConsoleOutputData;
import com.financeTracker.service.*;
import com.financeTracker.service.impl.*;
import com.financeTracker.util.Config;
import com.financeTracker.util.ConfigLoader;
import com.financeTracker.util.DatabaseManager;

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
