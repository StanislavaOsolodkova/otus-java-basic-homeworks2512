package ru.otus.java.basic.homeworks.hw29;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileSearchApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя файла: ");
        String fileName = scanner.nextLine();

        System.out.print("Введите искомую последовательность символов: ");
        String searchSequence = scanner.nextLine();

        try {
            int count = countOccurrencesInFile(fileName, searchSequence);
            System.out.println("Количество вхождений: " + count);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    public static int countOccurrencesInFile(String fileName, String search) throws IOException {
        String content = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
        return countOccurrences(content, search);
    }

    public static int countOccurrences(String text, String search) {
        if (search == null || search.isEmpty()) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(search, index)) != -1) {
            count++;
            index += search.length(); // сдвиг для непересекающихся вхождений
        }
        return count;
    }
}
