package ru.zinoviev.resthomework.db.service.impl;

import org.junit.jupiter.api.Test;
import ru.zinoviev.resthomework.H2TestStarter;
import ru.zinoviev.resthomework.db.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends H2TestStarter {

    @Test
    public void testGetAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        assertEquals(15, users.size());
    }

    @Test
    public void testGetUserById() {
        UserDto user = userService.getUserById(1);
        assertAll(
                () -> assertNotNull(user),
                () -> assertNotNull(user.getUserData()),
                () -> assertNotNull(user.getUserData().getAddress()),
                () -> assertNotNull(user.getUserData().getEmail()),
                () -> assertNotNull(user.getUserData().getTelegramName())
        );

        UserDto nullableUser = userService.getUserById(10000);

        assertAll(
                () -> assertEquals(-1, nullableUser.getId()),
                () -> assertNull(nullableUser.getName()),
                () -> assertNull(nullableUser.getLogin()),
                () -> assertNull(nullableUser.getUserData())
        );
    }

    @Test
    public void testGetUserByName() {
        List<UserDto> users = userService.findUserByName("ан");
        assertTrue(users.size() >= 3);

        users = userService.findUserByName(null);
        assertEquals(0, users.size());
    }


    @Test
    public void createUpdateDeleteUserTest() {
        List<UserDto> usersBeforeCreation = userService.getAllUsers();

        UserDto userDto = new UserDto();
        userDto.setLogin("MisterTwister");
        userDto.setName("NewOne");
        userService.createUser(userDto);
        List<UserDto> usersAfterCreation = userService.getAllUsers();
        assertTrue(usersAfterCreation.size() > usersBeforeCreation.size());
        UserDto newUser = userService.findUserByName("NewOne").get(0);
        assertEquals(userDto, newUser);

        newUser.setName("Updated");
        userService.updateUser(newUser);
        List<UserDto> usersAfterUpdate = userService.getAllUsers();
        assertAll(
                () -> assertEquals(usersAfterUpdate.size(), usersAfterCreation.size()),
                () -> assertEquals(0, userService.findUserByName("NewOne").size())
        );

        userService.deleteUserById(newUser.getId());
        List<UserDto> usersAfterDelete = userService.getAllUsers();
        assertAll(
                () -> assertEquals(usersAfterDelete.size(), usersBeforeCreation.size()),
                () -> assertEquals(0, userService.findUserByName("Updated").size())
        );
    }

}
