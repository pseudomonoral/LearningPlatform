package com.financeTracker.service.impl;

import com.financeTracker.model.entity.User;
import com.financeTracker.service.NotificationService;
import com.financeTracker.service.UserService;
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
