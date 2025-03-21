package com.learnPlatform.service.impl;

import com.learnPlatform.dao.UserDao;
import com.learnPlatform.exceptions.entity.EntityAlreadyExistException;
import com.learnPlatform.exceptions.entity.EntityNotFoundException;
import com.learnPlatform.model.entity.User;
import com.learnPlatform.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.learnPlatform.model.type.EntityName.USER;
import static com.learnPlatform.model.type.UserStatus.*;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
    }


    @Override
    public void deleteUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        userDao.delete(user.getId());
    }

    @Override
    public void updateUser(User user) {
        if (userDao.findById(user.getId()).isPresent()) {
            Optional<User> old = userDao.findByLogin(user.getLogin());
            if (old.isPresent() && !old.get().getId().equals(user.getId())) {
                throw new EntityAlreadyExistException(USER, user.getId());
            }
            userDao.update(user);
        }else {
            throw new EntityNotFoundException(USER, user.getId());
        }
    }

    @Override
    public void blockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(BLOCKED);
        userDao.update(user);
    }

    @Override
    public void unblockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(ACTIVE);
        userDao.update(user);
    }

    @Override
    public List<User> showAll() {
        return userDao.findAll();
    }
}
