package ru.bank.dao;

import ru.bank.domain.User;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class UserDao {

    private ConcurrentMap<UUID, User> users = new ConcurrentHashMap<>();

    public User getUser(UUID userId) {
        return users.get(userId);
    }

    public void saveUser(User user) {
        users.putIfAbsent(user.getId(), user);
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    public void deleteUser(UUID userId) {
        users.remove(userId);
    }
}

