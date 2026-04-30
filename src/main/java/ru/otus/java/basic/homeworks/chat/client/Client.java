package ru.otus.java.basic.homeworks.chat.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean running = true;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(30_000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Подключено к серверу " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            return;
        }

        // Поток приёма сообщений
        new Thread(() -> {
            try {
                while (running) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    if ("/exitok".equals(message)) {
                        System.out.println("Сервер подтвердил выход.");
                        running = false;
                        break;
                    }
                    System.out.println(message);
                }
            } catch (SocketTimeoutException e) {
                System.err.println("Таймаут соединения. Завершение.");
            } catch (IOException e) {
                if (running) {
                    System.err.println("Соединение потеряно: " + e.getMessage());
                }
            } finally {
                disconnect();
            }
        }, "Receiver").start();

        // Основной поток – чтение с консоли и отправка
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Вводите сообщения (/exit для выхода).");
            String line;
            while (running && (line = console.readLine()) != null) {
                out.println(line);
                if ("/exit".equals(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения консоли: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        running = false;
        try { if (in != null) in.close(); } catch (IOException ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}
        System.out.println("Клиент отключён.");
    }

    public static void main(String[] args) {
        new Client("localhost", 8189).start();
    }
}
