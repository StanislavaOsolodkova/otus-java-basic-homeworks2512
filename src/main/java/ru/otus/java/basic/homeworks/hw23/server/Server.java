package ru.otus.java.basic.homeworks.hw23.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final int port;
    private List<ClientHandler> clients;

    public Server(int port) {
        this.port = port;
        clients = new CopyOnWriteArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                subscribe(new ClientHandler(this, socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        broadcastMessage("Подключился пользователь " + clientHandler.getUsername());
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        broadcastMessage("Пользователь " + clientHandler.getUsername() + " покинул чат");
        clients.remove(clientHandler);
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

    /**
     * Отключает пользователя по нику (вызывается администратором).
     * @param username ник кикаемого пользователя
     * @return true, если пользователь был найден и отключён, иначе false
     */
    public boolean kickUser(String username) {
        ClientHandler target = getClientByUsername(username);
        if (target != null) {
            target.sendMsg("You have been kicked by admin");
            target.sendMsg("/exitok");  // чтобы клиент закрылся корректно
            target.disconnect();
            return true;
        }
        return false;
    }
}
