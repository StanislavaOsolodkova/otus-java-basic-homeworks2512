package ru.otus.java.basic.homeworks.chat.server;

public class BanManager {
    private final DatabaseService db;

    public BanManager(DatabaseService db) {
        this.db = db;
    }

    public void ban(String username, String duration, String byAdmin) {
        long banUntil;
        if ("perm".equals(duration)) {
            banUntil = Long.MAX_VALUE;
        } else {
            char unit = duration.charAt(duration.length() - 1);
            long val = Long.parseLong(duration.substring(0, duration.length() - 1));
            long millis = 0;
            switch (unit) {
                case 's':
                    millis = val * 1000L;
                    break;
                case 'm':
                    millis = val * 60_000L;
                    break;
                case 'h':
                    millis = val * 3_600_000L;
                    break;
                case 'd':
                    millis = val * 86_400_000L;
                    break;
                default:
                    throw new IllegalArgumentException("Неверный формат времени: " + duration);
            }
            banUntil = System.currentTimeMillis() / 1000 + millis / 1000;
        }
        db.setBan(username, banUntil);
        System.out.println("Пользователь " + username + " забанен до " + banUntil + " админом " + byAdmin);
    }

    public void unban(String username) {
        db.unbanUser(username);
    }

    public boolean isBanned(String username) {
        return db.isUserBanned(username);
    }

    public void expireBans() {
        db.expireBans();
    }
}
