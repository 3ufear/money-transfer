package ru.bank.service;

import io.micronaut.http.HttpStatus;
import ru.bank.dao.UserDao;
import ru.bank.domain.User;
import ru.bank.exception.BusinessException;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class UserService {

    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getById(UUID userId) {
        User user = userDao.getUser(userId);
        if (user == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "No User Found");
        }
        return user;
    }

    public User createUser(User user) {
        user.setId(UUID.randomUUID());
        userDao.saveUser(user);
        return user;
    }

    public User updateUser(User user) {
        getById(user.getId());
        userDao.updateUser(user);
        return user;
    }


    public void deleteUser(UUID userId) {
        getById(userId);
        userDao.deleteUser(userId);
    }
}
