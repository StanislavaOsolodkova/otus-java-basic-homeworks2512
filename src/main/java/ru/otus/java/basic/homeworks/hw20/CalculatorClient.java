package ru.otus.java.basic.homeworks.hw20;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CalculatorClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 65432;

    public static void main(String[] args) {
        System.out.println("Клиент калькулятора запущен");
        System.out.println("Подключение к серверу " + SERVER_ADDRESS + ":" + SERVER_PORT + "...");

        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Подключение установлено!");
            System.out.println("-".repeat(40));

            String opsMessage = in.readLine();
            System.out.println(opsMessage);
            System.out.println("-".repeat(40));

            while (true) {
                try {
                    System.out.print("Введите первое число (или 'exit' для выхода): ");
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Завершение работы...");
                        break;
                    }
                    double a = Double.parseDouble(input);

                    System.out.print("Введите операцию (+, -, *, /): ");
                    String op = scanner.nextLine().trim();

                    System.out.print("Введите второе число: ");
                    double b = Double.parseDouble(scanner.nextLine().trim());

                    String request = op + " " + a + " " + b;
                    out.println(request);
                    System.out.println("Запрос отправлен: " + request);

                    String response = in.readLine();
                    System.out.println("Результат: " + response);
                    System.out.println("-".repeat(40));

                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число!");
                    System.out.println("-".repeat(40));
                }
            }

        } catch (ConnectException e) {
            System.err.println("Ошибка: не удалось подключиться к серверу. Убедитесь, что сервер запущен.");
        } catch (IOException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        }
    }
}