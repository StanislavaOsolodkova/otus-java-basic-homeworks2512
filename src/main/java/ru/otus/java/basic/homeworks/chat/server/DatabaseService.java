package ru.otus.java.basic.homeworks.chat.server;

import ru.otus.java.basic.homeworks.chat.common.User;
import ru.otus.java.basic.homeworks.chat.common.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
            initTables();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }

    private void initTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password_hash TEXT NOT NULL, " +
                    "role TEXT DEFAULT 'user', " +
                    "created_at INTEGER DEFAULT (strftime('%s','now')), " +
                    "last_online INTEGER DEFAULT (strftime('%s','now')), " +
                    "is_banned INTEGER DEFAULT 0, " +
                    "ban_until INTEGER DEFAULT NULL, " +
                    "rating INTEGER DEFAULT 0, " +
                    "like_balance INTEGER DEFAULT 10)");

            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT UNIQUE NOT NULL, " +
                    "owner_id INTEGER NOT NULL, " +
                    "password_hash TEXT DEFAULT NULL, " +
                    "created_at INTEGER DEFAULT (strftime('%s','now')), " +
                    "last_active INTEGER DEFAULT (strftime('%s','now')), " +
                    "FOREIGN KEY (owner_id) REFERENCES users(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS messages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "room_name TEXT DEFAULT 'general', " +
                    "username TEXT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "is_private INTEGER DEFAULT 0, " +
                    "recipient TEXT DEFAULT NULL, " +
                    "sent_at INTEGER DEFAULT (strftime('%s','now')))");
        }
    }

    // === USERS ===
    public boolean registerUser(String username, String passwordHash) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // уже существует
        }
    }

    public User authenticate(String username, String passwordHash) {
        String sql = "SELECT id, username, role, is_banned, ban_until, rating, like_balance " +
                "FROM users WHERE username = ? AND password_hash = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean banned = rs.getInt("is_banned") == 1;
                long banUntil = rs.getLong("ban_until");
                if (banned && (banUntil == Long.MAX_VALUE || banUntil > System.currentTimeMillis() / 1000)) {
                    return null; // забанен
                }
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"),
                        rs.getInt("rating"), rs.getInt("like_balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLastOnline(String username) {
        String sql = "UPDATE users SET last_online = strftime('%s','now') WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getLastOnline(String username) {
        String sql = "SELECT last_online FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("last_online");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserRating(String username, int delta) {
        String sql = "UPDATE users SET rating = rating + ? WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLikeBalance(String username, int delta) {
        String sql = "UPDATE users SET like_balance = like_balance + ? WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLikeBalance(String username) {
        String sql = "SELECT like_balance FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("like_balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setBan(String username, long banUntilSeconds) {
        String sql = "UPDATE users SET is_banned = 1, ban_until = ? WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, banUntilSeconds);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unbanUser(String username) {
        String sql = "UPDATE users SET is_banned = 0, ban_until = NULL WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserBanned(String username) {
        String sql = "SELECT is_banned, ban_until FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("is_banned") == 1) {
                long banUntil = rs.getLong("ban_until");
                return banUntil > System.currentTimeMillis() / 1000 || banUntil == Long.MAX_VALUE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeNick(String oldNick, String newNick) {
        String sql = "UPDATE users SET username = ? WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newNick);
            ps.setString(2, oldNick);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // === ROOMS ===
    public boolean createRoom(String name, int ownerId, String passwordHash) {
        String sql = "INSERT INTO rooms (name, owner_id, password_hash) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, ownerId);
            if (passwordHash != null) ps.setString(3, passwordHash);
            else ps.setNull(3, Types.VARCHAR);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Room getRoom(String name) {
        String sql = "SELECT id, name, owner_id, password_hash, last_active FROM rooms WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Room(rs.getInt("id"), rs.getString("name"), rs.getInt("owner_id"),
                        rs.getString("password_hash"), rs.getLong("last_active"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getRoomList() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM rooms";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateRoomActivity(String name) {
        String sql = "UPDATE rooms SET last_active = strftime('%s','now') WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOldRooms(long thresholdSeconds) {
        String sql = "DELETE FROM rooms WHERE last_active < ? AND name != 'general'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, thresholdSeconds);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(String name) {
        try {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM rooms WHERE name = ?")) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM messages WHERE room_name = ?")) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === MESSAGES ===
    public void saveMessage(String room, String username, String content, boolean isPrivate, String recipient) {
        String sql = "INSERT INTO messages (room_name, username, content, is_private, recipient) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, room);
            ps.setString(2, username);
            ps.setString(3, content);
            ps.setInt(4, isPrivate ? 1 : 0);
            if (recipient != null) ps.setString(5, recipient);
            else ps.setNull(5, Types.VARCHAR);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRoomHistory(String roomName, int limit) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT username, content, sent_at FROM messages WHERE room_name = ? ORDER BY id DESC LIMIT ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, roomName);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String msg = String.format("[%s] %s: %s",
                        new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(rs.getLong("sent_at") * 1000)),
                        rs.getString("username"),
                        rs.getString("content"));
                history.add(0, msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public int countUserRooms(int userId) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE owner_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void expireBans() {
        String sql = "UPDATE users SET is_banned = 0, ban_until = NULL WHERE is_banned = 1 AND ban_until < ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, System.currentTimeMillis() / 1000);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
