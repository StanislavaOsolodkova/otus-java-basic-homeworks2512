package ru.otus.java.basic.homeworks.hw7;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] arr = new int[][]{{1, 2, 3, 4}, {-5, -6, -7, -8}, {9, 10, 11, 12}, {-13, -14, -15, -16}};
        int[][] arr2 = new int[][]{{1, 1, 1, 1}, {4, 4, 4, 4}, {7, 7, 7, 7}, {10, 10, 10, 10}};
        int result = sumOfPositiveElements(arr);
        System.out.println("Сумма положительных элементов равна " + result);

        System.out.println("Введите число от 2 до 5: ");
        int size = scanner.nextInt();
        System.out.println("Вы ввели значение: " + size);
        char[][] sqr = new char[size][size];
        System.out.println("Ваш квадрат:");
        printSquare(sqr, size);

        System.out.println("Исходный массив:");
        printArray(arr);
        zeroLeftDiagonal(arr);
        System.out.println("Обнуление левой диагонали:");
        printArray(arr);

        System.out.println("Исходный массив:");
        printArray(arr2);
        zeroRightDiagonal(arr2);
        System.out.println("Обнуление правой диагонали:");
        printArray(arr2);

        int max = findMax(arr);
        System.out.println("Максимальный элемент массива равен " + max);

        int sumOfSecondRow = sumOfSecondRow(arr);
        if (sumOfSecondRow != -1) {
            System.out.println("Сумма элементов второй строки массива равна " + sumOfSecondRow);
        } else {
            System.out.println("Второй строки в массиве не существует");
        }
    }

    //Задача 1
    public static int sumOfPositiveElements(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] > 0) {
                    sum += arr[i][j];
                }
            }
        }
        return sum;
    }

    //Задача 2
    public static void printSquare(char[][] sqr, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sqr[i][j] = '*';
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(sqr[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Задача 3
    public static void zeroLeftDiagonal(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i][i] = 0;
        }
    }

    //Задача 3.1
    public static void zeroRightDiagonal(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            int j = arr.length - 1 - i;
            arr[i][j] = 0;
        }
    }

    public static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.printf("%3d ", arr[i][j]);
            }
            System.out.println();
        }
    }

    //Задача 4
    public static int findMax(int[][] arr) {
        int max = arr[0][0];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] > max) {
                    max = arr[i][j];
                }
            }
        }
        return max;
    }

    //Задача 5
    public static int sumOfSecondRow(int[][] arr) {
        int sumOfSecondRow = 0;
        if (arr.length < 2) {
            return -1;
        } else {
            for (int j = 0; j < arr[1].length; j++) {
                sumOfSecondRow += arr[1][j];
            }
            return sumOfSecondRow;
        }
    }
}