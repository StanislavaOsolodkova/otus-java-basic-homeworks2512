package ru.otus.java.basic.homeworks.hw26.auth;

public interface AuthService {
    void start() throws Exception;
    void stop();
    String getNickByLoginPass(String login, String pass);
    boolean register(String login, String pass, String nick);
    boolean changeNick(String login, String newNick);
}
