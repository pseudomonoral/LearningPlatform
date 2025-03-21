package com.learnPlatform.service.impl;

import com.learnPlatform.model.entity.User;
import com.learnPlatform.service.NotificationService;
import com.learnPlatform.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;

    @Override
    public boolean notifyUserAchieved(User user) {
        return false;
    }

    @Override
    public boolean notifyMessageForUser(User user) {
        return false;
    }
}
