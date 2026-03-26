package ru.otus.java.basic.homeworks.hw15;

public class MainApplication {

    public static void main(String[] args) {
        String[][] array = {
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16"}
        };

        try {
            int sum = ArraySumCalculator.sumArrayElements(array);
            System.out.println("Сумма элементов: " + sum);
        } catch (AppArraySizeException | AppArrayDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}