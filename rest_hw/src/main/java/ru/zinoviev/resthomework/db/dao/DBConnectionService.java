package ru.zinoviev.resthomework.db.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionService implements ConnectionService {

    private final String propertiesFileName = "database.properties";

    @Override
    public Connection getConnection() {
        Connection connection;
        try {
            Properties props = new Properties();
            try (InputStream input = DBConnectionService.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                props.load(input);
            }
            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
