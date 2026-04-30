package ru.otus.java.basic.homeworks.chat.client;

public class ClientApp {
    public static void main(String[] args) {
        new Client("localhost", 8189).start();
    }
}
