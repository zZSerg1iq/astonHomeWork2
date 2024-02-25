package ru.zinoviev.resthomework.web.validator;

import ru.zinoviev.resthomework.db.dto.UserDto;

import java.io.IOException;

public class UserValidator {

    public static void validate(UserDto userDto) throws IOException {
        if (userDto.getName() != null && userDto.getName().equals("")
                || userDto.getLogin().equals("")
        ) {
            throw new IOException("Object validation exception");
        }
    }
}
