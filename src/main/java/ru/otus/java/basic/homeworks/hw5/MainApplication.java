package ru.otus.java.basic.homeworks.hw5;

import java.util.Arrays;

public class MainApplication {
    public static void main(String[] args) {
        printLine(5, "Hello");
        sumAndPrint(new int[]{1, 2, 5, 7, 3, 8, 6});
        fillingWithSpecifiedNumber(5, new int[]{1, 2, 5, 7, 3, 8, 6});
        increasingArray(10, new int[]{1, 2, 5, 7, 3, 8, 6});
        comparingElements(new int[]{9, 1, 2, 5, 7, 3, 8, 6});
        sumArraysAndPrint(new int[]{1, 2, 3}, new int[]{2, 2}, new int[]{1, 1, 1, 1, 1});
        findDot(new int[]{1,1,1,1,1,5});
        descendingSearch(new int[]{1, 2, 5, 7, 3, 8, 6});
        reverseArray(new int[]{1, 2, 5, 7, 3, 8, 6});
    }

    //Задача 1
    public static void printLine(int count, String text) {
        for (int i = 0; i < count; i++) {
            System.out.println(text);
        }
    }

    //Задача 2
    public static void sumAndPrint(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 5) {
                sum += arr[i];
            }
        }
        System.out.println("Сумма элементов, значение которых больше 5 равна " + sum);
    }

    //Задача 3
    public static void fillingWithSpecifiedNumber(int number, int[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = number;
        }
        System.out.println("Новый массив: " + Arrays.toString(input));
    }

    //Задача 4
    public static void increasingArray(int number, int[] inc) {
        for (int i = 0; i < inc.length; i++) {
            inc[i] += number;
        }
        System.out.println("Увеличенный массив: " + Arrays.toString(inc));
    }

    //Задача 5
    public static void comparingElements(int[] comp) {
        int sumLeft = 0;
        int sumRight = 0;
        for (int i = 0; i < comp.length / 2; i++) {
            sumLeft += comp[i];
        }
        for (int i = comp.length / 2; i < comp.length; i++) {
            sumRight += comp[i];
        }
        if (sumLeft > sumRight) {
            System.out.println("Сумма первой половины массива больше");
        } else if (sumRight > sumLeft) {
            System.out.println("Сумма второй половины массива больше");
        } else {
            System.out.println("Суммы половин массива равны");
        }
    }

    //Задача 6
    public static void sumArraysAndPrint(int[] arr1, int[] arr2, int[] arr3) {
        int maxLength = 0;
        if (arr1.length > maxLength) {
            maxLength = arr1.length;
        }
        if (arr2.length > maxLength) {
            maxLength = arr2.length;
        }
        if (arr3.length > maxLength) {
            maxLength = arr3.length;
        }
        int[] result = new int[maxLength];

        for (int i = 0; i < arr1.length; i++) {
            result[i] += arr1[i];
        }
        for (int i = 0; i < arr2.length; i++) {
            result[i] += arr2[i];
        }
        for (int i = 0; i < arr3.length; i++) {
            result[i] += arr3[i];
        }
        System.out.println("Сумма массивов равна новому массиву: " + Arrays.toString(result));
    }

    //Задача 7
    public static void findDot(int[] dot) {
        System.out.println("Элементы массива: " + java.util.Arrays.toString(dot));
        boolean found = false;
        for (int index = 1; index < dot.length; index++) {
            int leftSum = 0;
            int rightSum = 0;

            for (int i = 0; i < index; i++) {
                leftSum += dot[i];
            }

            for (int i = index; i < dot.length; i++) {
                rightSum += dot[i];
            }
            if (leftSum == rightSum) {
                System.out.println("Точка равновесия между индексами " + (index - 1) + " и " + index);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Точка равновесия отсутствует");
        }
    }

    //Задача 8
    public static void descendingSearch(int[] des) {
        System.out.println("Элементы массива: " + java.util.Arrays.toString(des));
        boolean result = true;
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] <= des[i + 1]) {
                result = false;
            }
        }
        if (!result) {
            System.out.println("Элементы массива не идут в порядке убывания");
        }
    }

    //Задача 9
    public static void reverseArray(int[] rev) {
        int[] reversed = new int[rev.length];
        for (int i = 0; i < rev.length; i++) {
            reversed[i] = rev[rev.length - 1 - i];
        }
        System.out.println("Исходный: " + Arrays.toString(rev));
        System.out.println("Перевернутый: " + Arrays.toString(reversed));
    }
}
