package ru.otus.java.basic.homeworks.hw19;

import java.io.*;
import java.util.Scanner;

public class FileManager {

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("=== Список текстовых файлов в корневом каталоге ===");
            listTextFiles();

            String filename;
            while (true) {
                System.out.print("\nВведите имя файла (или 'exit' для выхода): ");
                filename = scanner.nextLine().trim();

                if (filename.equalsIgnoreCase("exit")) {
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                }

                File file = new File(filename);
                if (file.exists() && file.isFile()) {
                    break;
                } else {
                    System.out.println("Файл не найден. Попробуйте снова.");
                    listTextFiles();
                }
            }

            System.out.println("\n=== Содержимое файла " + filename + " ===");
            readFile(filename);

            System.out.print("\nВведите строку для записи в файл: ");
            String userInput = scanner.nextLine();
            writeToFile(filename, userInput);

            System.out.println("\n=== Обновленное содержимое файла " + filename + " ===");
            readFile(filename);

            scanner.close();
        }

        private static void listTextFiles() {
            File currentDir = new File(".");
            File[] files = currentDir.listFiles();

            int count = 0;
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                        System.out.println("  📄 " + file.getName());
                        count++;
                    }
                }
            }

            if (count == 0) {
                System.out.println("  (Нет .txt файлов)");
            } else {
                System.out.println("  Всего найдено: " + count + " файл(ов)");
            }
        }

        private static void readFile(String filename) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                int lineNum = 1;
                boolean hasContent = false;

                while ((line = reader.readLine()) != null) {
                    System.out.println(lineNum + ": " + line);
                    lineNum++;
                    hasContent = true;
                }

                if (!hasContent) {
                    System.out.println("(Файл пуст)");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден: " + filename);
            } catch (IOException e) {
                System.out.println("Ошибка чтения: " + e.getMessage());
            }
        }

        private static void writeToFile(String filename, String content) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(content);
                writer.newLine();
                System.out.println("\n✅ Строка \"" + content + "\" успешно добавлена в файл " + filename);
            } catch (IOException e) {
                System.out.println("❌ Ошибка записи: " + e.getMessage());
            }
        }
    }
