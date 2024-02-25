package ru.zinoviev.resthomework.db.dto;

import lombok.Data;

import java.util.Objects;


@Data
public class UserDto {

    private long id = -1;

    private String name;

    private String login;

    private UserDataDto userData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(name, userDto.name) && Objects.equals(login, userDto.login) && Objects.equals(userData, userDto.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, userData);
    }
}
