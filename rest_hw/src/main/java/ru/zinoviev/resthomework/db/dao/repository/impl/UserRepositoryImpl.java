package ru.zinoviev.resthomework.db.dao.repository.impl;

import ru.zinoviev.resthomework.db.dao.entity.User;
import ru.zinoviev.resthomework.db.dao.entity.UserData;
import ru.zinoviev.resthomework.db.dao.ConnectionService;
import ru.zinoviev.resthomework.db.dao.repository.UserRepository;
import ru.zinoviev.resthomework.exception.CrudExceptionMessages;
import ru.zinoviev.resthomework.exception.CustomCRUDException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final String CREATE_USER = "INSERT INTO \"user\" (name, login) VALUES(?,?)";

    //one to one
    //одну сущность User содержит одну и только одну сущность UserData
    private final String GET_BY_ID =
            "SELECT u.id, u.name, u.login, ud.address, ud.telegram_name, ud.email " +
                    "FROM \"user\" u " +
                    "inner join user_data ud on u.id = ud.id " +
                    "WHERE u.id = ?";
    private final String GET_BY_NAME = "SELECT * FROM \"user\" WHERE name like CONCAT('%', ?, '%')";
    private final String GET_ALL = "SELECT * FROM \"user\" ";
    private final String UPDATE_USER = "UPDATE \"user\" set name = ?, login = ? where id = ?";
    private final String DELETE_USER = "delete from \"user\" where id = ?";
    private final String DELETE_USER_LIKES = "delete from user_likes where user_id = ?";

    private final ConnectionService connectionService;


    public UserRepositoryImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }


    @Override
    public void createUser(User user) {
        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement createStatement = connection.prepareStatement(CREATE_USER)) {
                createStatement.setString(1, user.getName());
                createStatement.setString(2, user.getLogin());

                int rowsInserted = createStatement.executeUpdate();

                if (rowsInserted == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.CREATION_ERROR.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage(), e);
        }
    }

    @Override
    public Optional<User> getUserById(long userId) {
        User user = null;
        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement findByIdStatement = connection.prepareStatement(GET_BY_ID)) {
                findByIdStatement.setLong(1, userId);
                ResultSet resultSet = findByIdStatement.executeQuery();

                if (resultSet.next()) {
                    user = new User();
                    user.setId(userId);
                    user.setName(resultSet.getString("name"));
                    user.setLogin(resultSet.getString("login"));

                    UserData userData = new UserData();
                    userData.setId(userId);
                    userData.setAddress(resultSet.getString("address"));
                    userData.setEmail(resultSet.getString("email"));
                    userData.setTelegramName(resultSet.getString("telegram_name"));
                    user.setUserData(userData);
                }

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.GET_ERROR.getMessage());
            }
        } catch (Exception e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage());
        }

        return Optional.ofNullable(user);
    }


    @Override
    public List<User> findUserByName(String userName) {
        List<User> users;
        try (Connection connection = connectionService.getConnection()) {

            try {
                PreparedStatement findByNameStatement = connection.prepareStatement(GET_BY_NAME);
                findByNameStatement.setString(1, userName);
                ResultSet resultSet = findByNameStatement.executeQuery();
                users = getUserListFromResultSet(resultSet);

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage(), e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage(), e);
        }

        return users;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try (Connection connection = connectionService.getConnection()) {

            try {
                PreparedStatement userListStatement = connection.prepareStatement(GET_ALL);
                ResultSet resultSet = userListStatement.executeQuery();
                users = getUserListFromResultSet(resultSet);

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage(), e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage(), e);
        }
        return users;
    }

    @Override
    public void updateUser(User user) {

        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement updateUserStatement = connection.prepareStatement(UPDATE_USER)) {
                updateUserStatement.setString(1, user.getName());
                updateUserStatement.setString(2, user.getLogin());
                updateUserStatement.setLong(3, user.getId());

                int updatedRow = updateUserStatement.executeUpdate();
                if (updatedRow == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.UPDATE_ERROR.getMessage());
                }
            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage(), e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage(), e);
        }
    }

    @Override
    public void deleteUserById(long userId) {
        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement removeUserLikes = connection.prepareStatement(DELETE_USER_LIKES)) {
                removeUserLikes.setLong(1, userId);
                removeUserLikes.executeUpdate();
            } catch (SQLException e) {
                System.out.println("USER LIKE " + e.getMessage());
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage());
            }

            try (PreparedStatement removeUser = connection.prepareStatement(DELETE_USER)) {
                removeUser.setLong(1, userId);
                int deletedRow = removeUser.executeUpdate();

                if (deletedRow == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.DELETE_ERROR.getMessage());
                }

            } catch (SQLException e) {
                System.out.println("USER " + e.getMessage());
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage());
            }


        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage(), e);
        }
    }

    private List<User> getUserListFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setLogin(resultSet.getString("login"));
            users.add(user);
        }

        return users;
    }
}
