package ru.zinoviev.resthomework.db.service.impl;

import ru.zinoviev.resthomework.db.dao.entity.User;
import ru.zinoviev.resthomework.db.dao.repository.UserRepository;
import ru.zinoviev.resthomework.db.dto.UserDto;
import ru.zinoviev.resthomework.db.mapper.UserMapper;
import ru.zinoviev.resthomework.db.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserDto user) {
        userRepository.createUser(new UserMapper().dtoToEntity(user));
    }

    @Override
    public UserDto getUserById(long userId) {
        return new UserMapper().entityToDto(userRepository.getUserById(userId).orElse(new User()));
    }

    @Override
    public List<UserDto> findUserByName(String userName) {
        if (userName == null) {
            return new ArrayList<>();
        }
        return new UserMapper().listEntityToListDto(userRepository.findUserByName(userName));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return new UserMapper().listEntityToListDto(userRepository.getAllUsers());
    }

    @Override
    public void updateUser(UserDto user) {
        userRepository.updateUser(new UserMapper().dtoToEntity(user));
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteUserById(userId);
    }
}
