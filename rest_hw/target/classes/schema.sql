DROP TABLE IF EXISTS "Users";
DROP TABLE IF EXISTS "Films";
DROP TABLE IF EXISTS "FriendList";
DROP TABLE IF EXISTS "UserLikes";

-- Создание таблицы User
CREATE TABLE IF NOT EXISTS Users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL,
                                     login VARCHAR(50) NOT NULL,
                                     email VARCHAR(50) NOT NULL,
                                     birthday Date
);

-- Создание таблицы Film
CREATE TABLE IF NOT EXISTS Films (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     filmname VARCHAR(50),
                                     description VARCHAR(200),
                                     release_date DATE,
                                     duration INT,
                                     genre VARCHAR(20),
                                     mpa_rate VARCHAR(20)
);

-- Создание таблицы FriendList
CREATE TABLE IF NOT EXISTS FriendList (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          user_id INT,
                                          friend_id INT,
                                          friend_request_status VARCHAR(20),
                                          FOREIGN KEY (user_id) REFERENCES Users(id),
                                          FOREIGN KEY (friend_id) REFERENCES Users(id)
);

-- Создание таблицы UserLikes
CREATE TABLE IF NOT EXISTS UserLikes (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         user_id INT,
                                         film_id INT,
                                         FOREIGN KEY (user_id) REFERENCES Users(id),
                                         FOREIGN KEY (film_id) REFERENCES Films(id)
);
