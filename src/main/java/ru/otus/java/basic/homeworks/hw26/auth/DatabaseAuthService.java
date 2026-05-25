package ru.otus.java.basic.homeworks.hw26.auth;

import java.sql.*;

public class DatabaseAuthService implements AuthService {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:chat_users.db";

    @Override
    public void start() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(DB_URL);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "nickname TEXT UNIQUE NOT NULL" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        String sql = "SELECT nickname FROM users WHERE login = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean register(String login, String pass, String nick) {
        String sql = "INSERT INTO users (login, password, nickname) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, nick);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeNick(String login, String newNick) {
        String sql = "UPDATE users SET nickname = ? WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newNick);
            ps.setString(2, login);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void stop() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
