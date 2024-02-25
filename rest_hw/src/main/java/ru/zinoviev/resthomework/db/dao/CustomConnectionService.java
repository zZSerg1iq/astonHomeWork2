package ru.zinoviev.resthomework.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnectionService implements ConnectionService {

    final String url;
    final String name;
    final String pass;

    public CustomConnectionService(String url, String name, String pass) {
        this.url = url;
        this.name = name;
        this.pass = pass;
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, name, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
