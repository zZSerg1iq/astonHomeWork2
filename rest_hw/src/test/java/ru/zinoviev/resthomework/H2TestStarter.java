package ru.zinoviev.resthomework;

import org.junit.jupiter.api.BeforeAll;
import ru.zinoviev.resthomework.db.DBServiceManager;
import ru.zinoviev.resthomework.db.service.FilmService;
import ru.zinoviev.resthomework.db.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class H2TestStarter {

    private final static String DROP_ALL_QUERY =
            "DROP TABLE IF EXISTS user_data CASCADE ; " +
                    "DROP TABLE IF EXISTS \"user\" CASCADE ;" +
                    "DROP TABLE IF EXISTS film CASCADE ;" +
                    "DROP TABLE IF EXISTS user_likes CASCADE ;";


    private final static String CREATE_USER_TABLES_QUERY =
            "CREATE TABLE IF NOT EXISTS user_data ( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "address VARCHAR(255) NOT NULL,  " +
                    "email VARCHAR(50) NOT NULL," +
                    "telegram_name VARCHAR(50) NOT NULL" +
                    "); " +


                    "CREATE TABLE IF NOT EXISTS \"user\" ( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "login VARCHAR(255) NOT NULL," +
                    "user_data_id INT," +
                    "CONSTRAINT \"user_data_id_fkey\" FOREIGN KEY (user_data_id) REFERENCES user_data(id) on delete cascade" +
                    ");";


    private final static String CREATE_FILM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS film (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "description VARCHAR(500)," +
                    "release_date DATE NOT NULL," +
                    "duration INTEGER NOT NULL" +
                    ");";

    private final static String CREATE_USER_LIKES_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS user_likes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id BIGINT NOT NULL," +
                    "film_id BIGINT NOT NULL," +
                    "\"comment\" VARCHAR(255) NOT NULL," +
                    "CONSTRAINT \"user_likes_user_id_fkey\" FOREIGN KEY (user_id) REFERENCES \"user\"(id)," +
                    "CONSTRAINT \"user_likes_film_id_fkey\" FOREIGN KEY (film_id) REFERENCES film(id)" +
                    ")";

    private final static String ADD_FILMS =
            "INSERT INTO film (name, description, release_date, duration) VALUES " +
                    "( 'Побег из Шоушенка', 'Двое заключенных в тюрьме строят дружбу и обретают надежду благодаря добрым поступкам друг к другу.', '1994-10-14', 142)," +
                    "( 'Зеленая миля', 'История о тюремном надзирателе, который развивает особые отношения с обвиняемым в убийстве, обладающем необъяснимой способностью.', '1999-03-10', 189)," +
                    "( 'Форрест Гамп', 'Джентльмен с необыкновенно низким коэффициентом интеллекта переживает множество значимых событий в своей жизни, изменяя к лучшему судьбу себя и других.', '1994-07-06', 142)," +
                    "( 'Поющие в дождь', 'Рассказывает о переходе от беззаботных дней немого кино к требованиям звуковой фильмы эпохи звукового кино.', '1952-04-11', 103)," +
                    "( 'Титаник', 'История о любви между парнем из нижнего класса и девушкой из высшего, у которых на пути друг к другу встаёт айсберг.', '1997-12-19', 195);";



    private final static String ADD_USER_LIKES =
            "INSERT INTO user_likes (user_id, film_id, \"comment\") VALUES" +
                    "('1', '2', 'Всё-таки сюжет мог быть лучше'), " +
                    "('1', '3', 'Отличный фильм!'), " +
                    "('1', '4', 'Очень интересно!'), " +
                    "('1', '1', 'Не понравилось..'), " +

                    "('2', '2', 'Отличный фильм!'), " +
                    "('2', '3', 'Неплохо, но не более'), " +
                    "('2', '4', 'ОНе понравилось..'), " +
                    "('2', '5', 'Отличный фильм!'), " +
                    "('2', '1', 'Отличный фильм!'), " +

                    "('3', '2', 'Очень интересно!'), " +
                    "('3', '3', 'Всё-таки сюжет мог быть лучше'), " +
                    "('3', '4', 'Отличный фильм!'), " +
                    "('3', '1', 'Отличный фильм!'), " +

                    "('4', '2', 'Всё-таки сюжет мог быть лучше'), " +
                    "('4', '3', 'Очень интересно!'), " +
                    "('4', '4', 'Не понравилось..'), " +
                    "('4', '5', 'Отличный фильм!'), " +

                    "('5', '2', 'Неплохо, но не более'), " +
                    "('5', '1', 'Очень интересно!'), " +
                    "('5', '2', 'Не понравилось..'), " +
                    "('5', '3', 'Отличный фильм!'), " +

                    "('6', '4', 'Отличный фильм!'), " +
                    "('6', '5', 'Отличный фильм!'), " +

                    "('7', '1', 'Всё-таки сюжет мог быть лучше'), " +
                    "('7', '2', 'Очень интересно!'), " +
                    "('7', '3', 'Отличный фильм!'), " +

                    "('8', '4', 'Неплохо, но не более'), " +
                    "('8', '5', 'Не понравилось..'), " +
                    "('8', '1', 'Отличный фильм!'), " +

                    "('9', '2', 'Всё-таки сюжет мог быть лучше'), " +
                    "('9', '3', 'Очень интересно!'), " +
                    "('9', '4', 'Отличный фильм!'), " +

                    "('10', '1', 'Отличный фильм!'), " +
                    "('10', '2', 'Очень интересно!'), " +
                    "('10', '3', 'Не понравилось..'), " +
                    "('10', '4', 'Всё-таки сюжет мог быть лучше'), " +
                    "('11', '5', 'Отличный фильм!'), " +
                    "('11', '1', 'Неплохо, но не более'), " +
                    "('11', '2', 'Отличный фильм!'), " +
                    "('11', '3', 'Очень интересно!'), " +
                    "('12', '4', 'Отличный фильм!'), " +
                    "('12', '1', 'Не понравилось..'), " +
                    "('12', '2', 'Отличный фильм!'), " +
                    "('13', '5', 'Неплохо, но не более'), " +
                    "('13', '2', 'Отличный фильм!'), " +
                    "('13', '3', 'Очень интересно!'), " +
                    "('14', '4', 'Отличный фильм!'), " +
                    "('14', '5', 'Не понравилось..'), " +
                    "('14', '1', 'Отличный фильм!'), " +
                    "('15', '2', 'Очень интересно!'), " +
                    "('15', '5', 'Не понравилось..'); ";



private final static String ADD_USER_DATA =
    "INSERT INTO user_data (id, address, email, telegram_name) VALUES            " +
            "(1, 'Москва, ул. Тверская, д. 10, кв. 5', 'user1@example.com', 'cooluser1')," +
            "(2, 'Санкт-Петербург, пр. Невский, д. 20, кв. 8', 'user2@example.com', 'awesomeuser2'),        " +
            "(3, 'Екатеринбург, ул. Ленина, д. 15, кв. 3', 'user3@example.com', 'fantasticuser3'),        " +
            "(4, 'Новосибирск, пр. Кирова, д. 30, кв. 12', 'user4@example.com', 'superuser4'),        " +
            "(5, 'Краснодар, ул. Красная, д. 5, кв. 7', 'user5@example.com', 'amazinguser5'),        " +
            "(6, 'Владивосток, пр. Центральный, д. 3, кв. 2', 'user6@example.com', 'excellentuser6'),        " +
            "(7, 'Казань, ул. Кремлевская, д. 8, кв. 14', 'user7@example.com', 'brilliantuser7'),       " +
            "(8, 'Ростов-на-Дону, ул. Большая, д. 17, кв. 4', 'user8@example.com', 'terrificuser8'),        " +
            "(9, 'Уфа, ул. Пушкина, д. 22, кв. 10', 'user9@example.com', 'ultimateuser9'),       " +
            " (10, 'Сочи, ул. Морская, д. 12, кв. 6', 'user10@example.com', 'smartuser10'),       " +
            " (11, 'Томск, пр. Ленинградский, д. 9, кв. 11', 'user11@example.com', 'cleveruser11'),       " +
            " (12, 'Омск, ул. Сибирская, д. 18, кв. 1', 'user12@example.com', 'geniususer12'),        " +
            "(13, 'Калининград, ул. Зеленая, д. 13, кв. 15', 'user13@example.com', 'heroicuser13'),     " +
            "   (14, 'Тула, ул. Октябрьская, д. 25, кв. 9', 'user14@example.com', 'legendaryuser14'),   " +
            "(15, 'Ярославль, пр. Ленина, д. 11, кв. 16', 'user15@example.com', 'mysterioususer15');";


    private final static String ADD_USERS =
            "INSERT INTO \"user\" (name, login, user_data_id) VALUES   " +
            "('Иван', 'ivan123', 1), " +
            "('Мария', 'maria456', 2),         " +
            "('Петр', 'petr789', 3),        " +
            "('Анна', 'anna321', 4),        " +
            "('Иван', 'vano654', 5),        " +
            "('Екатерина', 'ekaterina987', 6),        " +
            "('Дмитрий', 'dmitriy234', 7),        " +
            "('Ольга', 'olga567', 8),       " +
            "('Ваня', 'vanik890', 9),     " +
            "('Елена', 'elena432', 10),     " +
            "('Александр', 'aleksandr765', 11),  " +
            "('Татьяна', 'tatiana098', 12),      " +
            "('Сергей', 'sergey321', 13),      " +
            "('Юлия', 'yuliya654', 14),      " +
            "('Григорий', 'grigoriy987', 15);   ";





    private static final String URL = "jdbc:h2:file:./db/filmorate";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    protected static UserService userService;
    protected static FilmService filmService;


    @BeforeAll
    public static void setUp() {
        userService = DBServiceManager.getCustomUserService(URL, USERNAME, PASSWORD);
        filmService = DBServiceManager.getCustomFilmService(URL, USERNAME, PASSWORD);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            PreparedStatement drop = connection.prepareStatement(DROP_ALL_QUERY);
            drop.executeUpdate();

            PreparedStatement createUserTables = connection.prepareStatement(CREATE_USER_TABLES_QUERY);
            createUserTables.executeUpdate();

            PreparedStatement createFilmTables = connection.prepareStatement(CREATE_FILM_TABLE_QUERY);
            createFilmTables.executeUpdate();

            PreparedStatement createUserLikeTables = connection.prepareStatement(CREATE_USER_LIKES_TABLE_QUERY);
            createUserLikeTables.executeUpdate();

            PreparedStatement createUserLikeTable = connection.prepareStatement(CREATE_USER_LIKES_TABLE_QUERY);
            createUserLikeTable.executeUpdate();

            PreparedStatement addUserDataTableValues = connection.prepareStatement(ADD_USER_DATA);
            addUserDataTableValues.executeUpdate();

            PreparedStatement addUserTableValues = connection.prepareStatement(ADD_USERS);
            addUserTableValues.executeUpdate();

            PreparedStatement addFilmsTableValues = connection.prepareStatement(ADD_FILMS);
            addFilmsTableValues.executeUpdate();

            PreparedStatement addFilmsTableValues2 = connection.prepareStatement(ADD_USER_LIKES);
            addFilmsTableValues2.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
