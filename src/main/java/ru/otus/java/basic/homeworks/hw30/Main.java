package ru.otus.java.basic.homeworks.hw30;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int REPEATS = 5; // сколько раз напечатать ABC

    public static void main(String[] args) {
        SequentialPrinter printer = new SequentialPrinter();
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // Задача для буквы A
        pool.submit(() -> {
            for (int i = 0; i < REPEATS; i++) {
                printer.print('A', 0, REPEATS * 3);
            }
        });

        // Задача для буквы B
        pool.submit(() -> {
            for (int i = 0; i < REPEATS; i++) {
                printer.print('B', 1, REPEATS * 3);
            }
        });

        // Задача для буквы C
        pool.submit(() -> {
            for (int i = 0; i < REPEATS; i++) {
                printer.print('C', 2, REPEATS * 3);
            }
        });

        // Останавливаем пул после завершения всех задач
        pool.shutdown();
        try {
            // ждём, пока все задачи закончатся, но не бесконечно
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }

        System.out.println(); // перевод строки для красоты
        System.out.println("Программа завершена");
    }
}
