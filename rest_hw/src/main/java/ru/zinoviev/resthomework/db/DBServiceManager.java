package ru.zinoviev.resthomework.db;

import ru.zinoviev.resthomework.db.dao.CustomConnectionService;
import ru.zinoviev.resthomework.db.dao.DBConnectionService;
import ru.zinoviev.resthomework.db.dao.ConnectionService;
import ru.zinoviev.resthomework.db.dao.repository.FilmRepository;
import ru.zinoviev.resthomework.db.dao.repository.UserRepository;
import ru.zinoviev.resthomework.db.dao.repository.impl.FilmRepositoryImpl;
import ru.zinoviev.resthomework.db.dao.repository.impl.UserRepositoryImpl;
import ru.zinoviev.resthomework.db.service.FilmService;
import ru.zinoviev.resthomework.db.service.UserService;
import ru.zinoviev.resthomework.db.service.impl.FilmServiceImpl;
import ru.zinoviev.resthomework.db.service.impl.UserServiceImpl;

public class DBServiceManager {

    private static ConnectionService connectionService;
    private static FilmService filmService;
    private static FilmRepository filmRepository;
    private static UserService userService;
    private static UserRepository userRepository;

    public static UserService getDefaultUserService() {
        prepareInstance();
        return userService;
    }

    public static UserService getCustomUserService(final String url, final String name, final String pass) {
        prepareCustomInstance(url, name, pass);
        return userService;
    }

    public static FilmService getDefaultFilmService() {
        prepareInstance();
        return filmService;
    }

    public static FilmService getCustomFilmService(final String url, final String name, final String pass) {
        prepareCustomInstance(url, name, pass);
        return filmService;
    }

    private static void prepareInstance() {
        if (connectionService == null) {
            connectionService = new DBConnectionService();

            userRepository = new UserRepositoryImpl(connectionService);
            filmRepository = new FilmRepositoryImpl(connectionService);

            userService = new UserServiceImpl(userRepository);
            filmService = new FilmServiceImpl(filmRepository);
        }
    }

    private static void prepareCustomInstance(final String url, final String name, final String pass) {
        if (connectionService == null) {
            connectionService = new CustomConnectionService(url, name, pass);

            userRepository = new UserRepositoryImpl(connectionService);
            filmRepository = new FilmRepositoryImpl(connectionService);

            userService = new UserServiceImpl(userRepository);
            filmService = new FilmServiceImpl(filmRepository);
        }
    }
}
