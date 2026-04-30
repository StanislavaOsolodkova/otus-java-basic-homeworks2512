package ru.otus.java.basic.homeworks.chat.common;

public class Room {
    public final int id;
    public final String name;
    public final int ownerId;
    public final String passwordHash; // хеш или null
    public final long lastActive;     // unixtime

    public Room(int id, String name, int ownerId, String passwordHash, long lastActive) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.passwordHash = passwordHash;
        this.lastActive = lastActive;
    }

    public boolean hasPassword() {
        return passwordHash != null && !passwordHash.isEmpty();
    }
}
