package ru.otus.java.basic.homeworks.hw31.main;

import java.util.Arrays;

public class ArrayUtils {
    public static int[] getElementsAfterLastOne(int[] array) {
        if (array == null) {
            throw new RuntimeException("Входной массив не должен быть null");
        }
        int lastIndex = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 1) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex == -1) {
            throw new RuntimeException("Массив не содержит ни одной единицы");
        }
        return Arrays.copyOfRange(array, lastIndex + 1, array.length);
    }

    public static boolean checkArrayOnly1And2(int[] array) {
        if (array == null || array.length == 0) {
            return false;
        }
        boolean has1 = false;
        boolean has2 = false;
        for (int value : array) {
            if (value != 1 && value != 2) {
                return false;
            }
            if (value == 1) {
                has1 = true;
            } else {
                has2 = true;
            }
        }
        return has1 && has2;
    }
}
