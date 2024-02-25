package ru.zinoviev.resthomework.db.dao.repository.impl;

import ru.zinoviev.resthomework.db.dao.ConnectionService;
import ru.zinoviev.resthomework.db.dao.entity.Film;
import ru.zinoviev.resthomework.db.dao.entity.User;
import ru.zinoviev.resthomework.db.dao.entity.UserLike;
import ru.zinoviev.resthomework.db.dao.repository.FilmRepository;
import ru.zinoviev.resthomework.exception.CrudExceptionMessages;
import ru.zinoviev.resthomework.exception.CustomCRUDException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmRepositoryImpl implements FilmRepository {

    private final ConnectionService connectionService;

    private final String CREATE_FILM = "INSERT INTO film (name, description, release_date, duration) VALUES(?,?,?,?)";
    private final String GET_BY_ID = "SELECT * FROM film WHERE id = ?";

    //one to many
    //одну сущность Film содержит множество сущностей UserLike
    private final String GET_FILM_AND_USER_LIKES = "SELECT u.id, u.name, u.login, ul.\"comment\" " +
            "FROM \"user\" u  " +
            "INNER JOIN user_likes ul ON u.id = ul.user_id " +
            "WHERE ul.film_id = ? " +
            "LIMIT ? OFFSET ?;";


    private final String FIND_BY_NAME = "SELECT * FROM film WHERE name like CONCAT('%', ?, '%')";
    private final String ADD_EDER_LIKE = "INSERT INTO user_likes(user_id, film_id, comment) values (?,?,?)";
    private final String GET_ALL = "SELECT * FROM film";
    private final String UPDATE_FILM = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
    private final String DELETE_FILM_LIKES = "delete from user_likes where film_id = ?";
    final String DELETE_FILM = "delete from film where id = ?";


    public FilmRepositoryImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public void createFilm(Film film) {
        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(CREATE_FILM)) {
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, new Date(film.getReleaseDate().getTime()));
                statement.setInt(4, film.getDuration());

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.CREATION_ERROR.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }
    }


    @Override
    public Optional<Film> getFilmById(long filmId) {
        int defaultLimit = 50, defaultOffset = 0;
        Film film = null;
        List<Film> films = new ArrayList<>();
        try (Connection connection = connectionService.getConnection()) {

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
                preparedStatement.setLong(1, filmId);
                ResultSet resultSet = preparedStatement.executeQuery();

                films = getFilmListFromResultSet(resultSet);

                if (!films.isEmpty()) {
                    film = films.get(0);
                    film.setUserLikes(getUserLikes(connection, filmId, defaultLimit, defaultOffset));
                }
            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.GET_ERROR.getMessage() + " :" + films + ": " + e.getMessage());
            }


        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + " : " + e.getMessage());
        }

        return Optional.ofNullable(film);
    }


    @Override
    public List<UserLike> getFilmUserLikes(long filmId, int limit, int offset) {
        List<UserLike> userLikes;

        try (Connection connection = connectionService.getConnection()) {
            userLikes = getUserLikes(connection, filmId, limit, offset);
        } catch (Exception e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }

        return userLikes;
    }

    //one to many
    private List<UserLike> getUserLikes(Connection connection, long filmId, int limit, int offset) throws SQLException {
        List<UserLike> userLikes = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_FILM_AND_USER_LIKES)) {

            preparedStatement.setLong(1, filmId);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    UserLike userLike = new UserLike();
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLogin(resultSet.getString("login"));
                    userLike.setUser(user);
                    userLike.setComment(resultSet.getString("comment"));

                    userLikes.add(userLike);
                }
            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.GET_ERROR.getMessage() + ": " + e);
            }
        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage() + ": " + e);
        }
        return userLikes;
    }

    @Override

    public List<Film> findFilmByName(String filmName) {
        List<Film> films;
        try (Connection connection = connectionService.getConnection()) {

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME);
                preparedStatement.setString(1, filmName);
                ResultSet resultSet = preparedStatement.executeQuery();
                films = getFilmListFromResultSet(resultSet);

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.GET_ERROR.getMessage() + ": " + e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }

        return films;
    }

    @Override
    public void addUserLike(long filmId, long userId, String comment) {
        try (Connection connection = connectionService.getConnection()) {

            try {
                PreparedStatement statement =
                        connection.prepareStatement(ADD_EDER_LIKE);
                statement.setLong(1, userId);
                statement.setLong(2, filmId);
                statement.setString(3, comment);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.CREATION_ERROR.getMessage());
                }

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage() + ": " + e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films;

        try (Connection connection = connectionService.getConnection()) {

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
                ResultSet resultSet = preparedStatement.executeQuery();
                films = getFilmListFromResultSet(resultSet);

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage() + ": " + e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }

        return films;
    }

    @Override
    public void updateFilm(Film film) {
        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_FILM)) {
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, new Date(film.getReleaseDate().getTime()));
                statement.setInt(4, film.getDuration());
                statement.setLong(5, film.getId());

                int updatedRow = statement.executeUpdate();
                if (updatedRow == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.UPDATE_ERROR.getMessage());
                }

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage() + ": " + e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }
    }

    @Override
    public void deleteFilmById(long filmId) {
        try (Connection connection = connectionService.getConnection()) {

            try (PreparedStatement removeUserLikes = connection.prepareStatement(DELETE_FILM_LIKES)) {
                removeUserLikes.setLong(1, filmId);
                removeUserLikes.executeUpdate();
            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage());
            }

            try (PreparedStatement removeFilm = connection.prepareStatement(DELETE_FILM)) {
                removeFilm.setLong(1, filmId);

                int deletedRow = removeFilm.executeUpdate();

                if (deletedRow == 0) {
                    throw new CustomCRUDException(CrudExceptionMessages.DELETE_ERROR.getMessage());
                }

            } catch (SQLException e) {
                throw new CustomCRUDException(CrudExceptionMessages.QUERY_ERROR.getMessage() + ": " + e);
            }

        } catch (SQLException e) {
            throw new CustomCRUDException(CrudExceptionMessages.CONNECTION_ERROR.getMessage() + ": " + e);
        }
    }

    private List<Film> getFilmListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Film> films = new ArrayList<>();

        while (resultSet.next()) {
            Film film = new Film();
            film.setId(resultSet.getInt("id"));
            film.setName(resultSet.getString("name"));
            film.setDescription(resultSet.getString("description"));
            film.setReleaseDate(resultSet.getDate("release_date"));
            film.setDuration(resultSet.getInt("duration"));
            films.add(film);
        }

        return films;
    }
}
