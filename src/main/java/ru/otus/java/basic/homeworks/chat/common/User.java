package ru.otus.java.basic.homeworks.chat.common;

public class User {
    public final int id;
    public final String username;
    public final String role;
    public final int rating;
    public final int likeBalance;

    public User(int id, String username, String role, int rating, int likeBalance) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.rating = rating;
        this.likeBalance = likeBalance;
    }
}