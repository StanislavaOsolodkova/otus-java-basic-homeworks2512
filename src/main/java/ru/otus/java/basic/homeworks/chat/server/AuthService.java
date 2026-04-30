package ru.otus.java.basic.homeworks.chat.server;

import ru.otus.java.basic.homeworks.chat.common.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    private final DatabaseService db;

    public AuthService(DatabaseService db) {
        this.db = db;
    }

    public boolean register(String username, String password) {
        String hash = sha256(password);
        return db.registerUser(username, hash);
    }

    public User authenticate(String username, String password) {
        String hash = sha256(password);
        return db.authenticate(username, hash);
    }

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
