package ru.zinoviev.resthomework.db.service;

import ru.zinoviev.resthomework.db.dto.UserDto;

import java.util.List;

public interface UserService {


    void createUser(UserDto user);

    UserDto getUserById(long userId);

    List<UserDto> findUserByName(String userName);

    List<UserDto> getAllUsers();

    void updateUser(UserDto user);

    void deleteUserById(long userId);
}
