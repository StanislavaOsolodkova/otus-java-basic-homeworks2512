package ru.otus.java.basic.homeworks.hw20;

import java.io.*;
import java.net.*;
import java.util.*;

public class CalculatorServer {
    private static final int PORT = 65432;
    private static final Map<String, Operation> OPERATIONS = new HashMap<>();

    static {
        OPERATIONS.put("+", (a, b) -> a + b);
        OPERATIONS.put("-", (a, b) -> a - b);
        OPERATIONS.put("*", (a, b) -> a * b);
        OPERATIONS.put("/", (a, b) -> {
            if (b == 0) throw new ArithmeticException("Деление на ноль");
            return a / b;
        });
    }

    @FunctionalInterface
    interface Operation {
        double apply(double a, double b) throws ArithmeticException;
    }

    public static void main(String[] args) {
        System.out.println("Сервер запускается на порту " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключений...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен клиент: " + clientSocket.getInetAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true)
        ) {
            String opsMessage = "Доступные операции: " + String.join(", ", OPERATIONS.keySet());
            out.println(opsMessage);
            System.out.println("Отправлен список операций клиенту");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено от клиента: " + inputLine);

                try {
                    String[] parts = inputLine.trim().split("\\s+");
                    if (parts.length != 3) {
                        out.println("Ошибка: неверный формат. Используйте: операция число1 число2");
                        continue;
                    }

                    String op = parts[0];
                    double a = Double.parseDouble(parts[1]);
                    double b = Double.parseDouble(parts[2]);

                    Operation operation = OPERATIONS.get(op);
                    if (operation == null) {
                        out.println("Ошибка: неверная операция");
                    } else {
                        try {
                            double result = operation.apply(a, b);
                            // 4. Возвращаем результат клиенту
                            out.println(result);
                            System.out.println("Отправлен результат: " + result);
                        } catch (ArithmeticException e) {
                            out.println("Ошибка: " + e.getMessage());
                        }
                    }
                } catch (NumberFormatException e) {
                    out.println("Ошибка: некорректные числа");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при работе с клиентом: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Клиент отключился");
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }
}
