package ru.otus.java.basic.homeworks.hw26.server;

import ru.otus.java.basic.homeworks.hw26.auth.AuthService;
import ru.otus.java.basic.homeworks.hw26.auth.DatabaseAuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final int port;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final AuthService authService;

    public Server(int port) {
        this.port = port;
        this.authService = new DatabaseAuthService();
        try {
            authService.start();
            System.out.println("Сервис аутентификации запущен");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка запуска сервиса аутентификации", e);
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер чата запущен на порту " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                // Создаём обработчик, но в чат он попадёт только после аутентификации
                new ClientHandler(this, socket, authService);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            authService.stop();
        }
    }

    // Вызывается из ClientHandler после успешной аутентификации
    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastMessage("Подключился пользователь " + clientHandler.getUsername());
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastMessage("Пользователь " + clientHandler.getUsername() + " покинул чат");
    }

    public void broadcastMessage(String message) {
        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }

    public ClientHandler getClientByUsername(String username) {
        for (ClientHandler c : clients) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    public boolean kickUser(String username) {
        ClientHandler target = getClientByUsername(username);
        if (target != null) {
            target.sendMsg("Вас отключил администратор");
            target.sendMsg("/exitok");
            target.disconnect();
            return true;
        }
        return false;
    }
}
