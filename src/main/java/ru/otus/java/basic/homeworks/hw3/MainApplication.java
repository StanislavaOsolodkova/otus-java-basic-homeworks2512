package ru.otus.java.basic.homeworks.hw3;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите число от 1 до 5: ");
        int result = scanner.nextInt();
        System.out.println("Вы ввели значение: " + result);

        int rnd1 = (int) (Math.random() * 200) - 100;
        int rnd2 = (int) (Math.random() * 200) - 100;
        int rnd3 = (int) (Math.random() * 200) - 100;
        int rnd4 = (int) (Math.random() * 20);
        int rnd5 = (int) (Math.random() * 10);
        int rnd6 = (int) (Math.random() * 10);
        boolean rndBool = Math.random() > 0.5;


        if (result == 1) {
            greetings();
        }
        if (result == 2) {
            checkSign(rnd1, rnd2, rnd3);
        }
        if (result == 3) {
            selectColor(rnd4);
        }
        if (result == 4) {
            compareNumbers(rnd1, rnd2);
        } else {
            addOrSubtractAndPrint(rnd5, rnd6, rndBool);
        }
    }

    //Задача 1
    public static void greetings() {
        System.out.println("Hello");
        System.out.println("World");
        System.out.println("from");
        System.out.println("Java");
    }

    //Задача 2
    public static void checkSign(int a, int b, int c) {
        int sum = a + b + c;
        if (sum >= 0) {
            System.out.println("Сумма положительная");
        } else {
            System.out.println("Сумма отрицательная");
        }
    }

    //Задача 3
    public static void selectColor(int data) {
        if (data <= 10) {
            System.out.println("Красный");
        }
        if (data > 10 && data <= 20) {
            System.out.println("Желтый");
        }
        if (data > 20) {
            System.out.println("Зеленый");
        }
    }

    //Задача 4
    public static void compareNumbers(int a, int b) {
        if (a >= b) {
            System.out.println("a>=b");
        } else {
            System.out.println("a<b");
        }
    }

    //Задача 5
    public static void addOrSubtractAndPrint(int initValue, int delta, boolean increment) {
        int result;

        if (increment) {
            result = initValue + delta;
            System.out.println("Результат сложения: " + result);
        } else {
            result = initValue - delta;
            System.out.println("Результат вычитания: " + result);
        }

    }
}