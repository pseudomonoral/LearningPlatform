package com.learnPlatform.constants;

public interface ConsoleConstants {

    String SECURITY_MENU = """
            Введите одно число без пробелов или других символов:
            1. Регистрация
            2. Вход в систему
            3. Завершить программу
            """;

    String USER_SETTINGS_MENU = """
                Введите одно число без пробелов и других символов:
                1. Изменить имя профиля
                2. Сбросить пароль и email
                3. Удалить профиль
                0. Вернуться в главное меню
                """;


    String ADMIN_MENU = """
                Введите одно число без пробелов и других символов:
                1. Показать список зарегистрированных пользователей
                2. Заблокировать пользователя
                3. Разблокировать пользователя
                0. Вернуться в главное меню
                """;

    String MAIN_MENU = """
                Введите одно число без пробелов и других символов:
                1. Настройки пользователя
                2. Меню администратора
                3. Выйти из учётной записи
                0. Выйти из программы
                """;
}
