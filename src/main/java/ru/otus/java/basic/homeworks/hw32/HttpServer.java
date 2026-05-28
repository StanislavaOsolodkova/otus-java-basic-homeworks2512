package ru.otus.java.basic.homeworks.hw32;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        // try-with-resources для ServerSocket, чтобы он закрылся при завершении программы
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);

            while (true) {
                try {
                    // Ожидаем входящее соединение
                    Socket socket = serverSocket.accept();
                    System.out.println("Подключился клиент: " + socket.getInetAddress());

                    // Создаём поток для обработки клиента
                    new Thread(() -> handleClient(socket)).start();
                } catch (IOException e) {
                    System.err.println("Ошибка при приёме клиента: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Не удалось запустить сервер: " + e.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        // Обработка клиента в отдельном потоке
        try (socket) {  // try-with-resources автоматически закроет сокет после выхода из блока
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[8192];
            int n = inputStream.read(buffer);
            if (n < 1) {
                return;
            }
            String rawRequest = new String(buffer, 0, n);
            HttpRequest request = new HttpRequest(rawRequest);
            request.info(true);

            dispatcher.execute(request, outputStream);
        } catch (IOException e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
        }
    }
}
