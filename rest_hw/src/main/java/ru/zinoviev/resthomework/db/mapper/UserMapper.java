package ru.zinoviev.resthomework.db.mapper;

import ru.zinoviev.resthomework.db.dao.entity.User;
import ru.zinoviev.resthomework.db.dao.entity.UserData;
import ru.zinoviev.resthomework.db.dto.UserDataDto;
import ru.zinoviev.resthomework.db.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public List<UserDto> listEntityToListDto(List<User> UserList) {
        List<UserDto> UserDtoList = new ArrayList<>();

        for (User User : UserList) {
            UserDtoList.add(entityToDto(User));
        }

        return UserDtoList;
    }

    public User dtoToEntity(UserDto UserDto) {
        User User = new User();
        User.setId(UserDto.getId());
        User.setName(UserDto.getName());
        User.setLogin(UserDto.getLogin());

        return User;
    }

    public UserDto entityToDto(User user) {
        UserDto UserDto = new UserDto();
        if (user.getId() < 1) {
            return UserDto;
        }

        UserDto.setId(user.getId());
        UserDto.setName(user.getName());
        UserDto.setLogin(user.getLogin());

        if (user.getUserData() != null) {
            UserDto.setUserData(getUserDataDto(user.getUserData()));
        }

        return UserDto;
    }

    public UserDataDto getUserDataDto(UserData userData) {
        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setId(userData.getId());
        userDataDto.setAddress(userData.getAddress());
        userDataDto.setEmail(userData.getEmail());
        userDataDto.setTelegramName(userData.getTelegramName());
        return userDataDto;
    }


}
