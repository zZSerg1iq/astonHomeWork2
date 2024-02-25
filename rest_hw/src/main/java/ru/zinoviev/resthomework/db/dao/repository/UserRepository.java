package ru.zinoviev.resthomework.db.dao.repository;

import ru.zinoviev.resthomework.db.dao.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {


    void createUser(User user);

    Optional<User> getUserById(long userId);

    List<User> findUserByName(String userName);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUserById(long userId);
}
