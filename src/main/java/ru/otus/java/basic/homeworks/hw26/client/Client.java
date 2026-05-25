package ru.otus.java.basic.homeworks.hw26.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner sc;

    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        sc = new Scanner(System.in);

        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // --- ЭТАП АУТЕНТИФИКАЦИИ ---
            authenticate();

            // Поток чтения сообщений от сервера
            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (message.startsWith("/")) {
                            if (message.equals("/exitok")) {
                                break;
                            }
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            }).start();

            // Основной поток отправки сообщений
            while (true) {
                String message = sc.nextLine();
                out.writeUTF(message);
                if (message.equals("/exit")) {
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    private void authenticate() throws IOException {
        System.out.println("Добро пожаловать в чат! Введите:");
        System.out.println("  /auth login password");
        System.out.println("  /reg login password nickname");

        while (true) {
            String command = sc.nextLine();
            out.writeUTF(command);
            String response = in.readUTF();
            System.out.println("Сервер: " + response);
            if (response.startsWith("/auth_ok")) {
                break;
            }
            // иначе повторяем попытку
        }
        System.out.println("Аутентификация пройдена. Можно общаться.");
    }

    private void disconnect() {
        try {
            if (in != null) in.close();
        } catch (IOException e) { e.printStackTrace(); }
        try {
            if (out != null) out.close();
        } catch (IOException e) { e.printStackTrace(); }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
        if (sc != null) {
            sc.close();
        }
    }
}
