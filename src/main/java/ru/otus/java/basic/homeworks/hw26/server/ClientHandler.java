package ru.otus.java.basic.homeworks.hw26.server;


import ru.otus.java.basic.homeworks.hw26.auth.AuthService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final AuthService authService;

    private String username;
    private String role;   // "ADMIN" или "USER"

    public ClientHandler(Server server, Socket socket, AuthService authService) throws IOException {
        this.server = server;
        this.socket = socket;
        this.authService = authService;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        System.out.println("Новое подключение: " + socket.getPort());

        // Запускаем поток обработки (сначала аутентификация, потом чат)
        new Thread(() -> {
            try {
                // -------- ЭТАП АУТЕНТИФИКАЦИИ --------
                while (true) {
                    String msg = in.readUTF();
                    if (msg.startsWith("/auth ")) {
                        String[] parts = msg.split("\\s+", 3);
                        if (parts.length == 3) {
                            String nick = authService.getNickByLoginPass(parts[1], parts[2]);
                            if (nick != null) {
                                username = nick;
                                // Определяем роль
                                role = "admin".equalsIgnoreCase(username) ? "ADMIN" : "USER";
                                sendMsg("/auth_ok " + username);
                                break;  // успех – выходим из цикла аутентификации
                            } else {
                                sendMsg("/auth_fail Неверный логин или пароль");
                            }
                        } else {
                            sendMsg("/auth_fail Формат: /auth login password");
                        }
                    } else if (msg.startsWith("/reg ")) {
                        String[] parts = msg.split("\\s+", 4);
                        if (parts.length == 4) {
                            boolean success = authService.register(parts[1], parts[2], parts[3]);
                            if (success) {
                                sendMsg("/reg_ok Регистрация успешна. Теперь войдите: /auth login password");
                            } else {
                                sendMsg("/reg_fail Логин или ник уже заняты");
                            }
                        } else {
                            sendMsg("/reg_fail Формат: /reg login password nickname");
                        }
                    } else {
                        sendMsg("/auth_req Сначала выполните вход: /auth login password");
                    }
                }

                // Только теперь добавляем клиента в общий список
                server.addClient(this);
                System.out.println("Пользователь аутентифицирован: " + username + " (роль: " + role + ")");

                // -------- ОСНОВНОЙ ЦИКЛ ЧАТА --------
                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMsg("/exitok");
                            break;
                        } else if (message.startsWith("/w ")) {
                            String[] parts = message.split(" ", 3);
                            if (parts.length < 3) {
                                sendMsg("Формат: /w <ник> <сообщение>");
                            } else {
                                String targetNick = parts[1];
                                String privateMessage = parts[2];
                                ClientHandler target = server.getClientByUsername(targetNick);
                                if (target != null) {
                                    target.sendMsg("[Private] " + username + ": " + privateMessage);
                                    sendMsg("[Private to " + targetNick + "] " + username + ": " + privateMessage);
                                } else {
                                    sendMsg("Пользователь '" + targetNick + "' не найден");
                                }
                            }
                        } else if (message.startsWith("/kick ")) {
                            if (!"ADMIN".equals(role)) {
                                sendMsg("Недостаточно прав");
                            } else {
                                String[] parts = message.split(" ", 2);
                                if (parts.length < 2) {
                                    sendMsg("Формат: /kick <username>");
                                } else {
                                    String userToKick = parts[1];
                                    if (server.kickUser(userToKick)) {
                                        sendMsg("Пользователь " + userToKick + " отключён.");
                                    } else {
                                        sendMsg("Пользователь '" + userToKick + "' не найден.");
                                    }
                                }
                            }
                        } else {
                            sendMsg("Неизвестная команда: " + message);
                        }
                    } else {
                        server.broadcastMessage(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    public void sendMsg(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void disconnect() {
        server.removeClient(this);
        System.out.println("Клиент отключился: " + socket.getPort());
        try {
            if (in != null) in.close();
        } catch (IOException e) { e.printStackTrace(); }
        try {
            if (out != null) out.close();
        } catch (IOException e) { e.printStackTrace(); }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
