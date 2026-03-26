package ru.otus.java.basic.homeworks.hw15;

public class ArraySumCalculator {

    public static int sumArrayElements(String[][] array)
            throws AppArraySizeException, AppArrayDataException {

        if (array.length != 4) {
            throw new AppArraySizeException("Массив должен быть размером 4x4");
        }

        for (int i = 0; i < 4; i++) {
            if (array[i].length != 4) {
                throw new AppArraySizeException("Массив должен быть размером 4x4");
            }
        }

        int sum = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                } catch (NumberFormatException e) {
                    throw new AppArrayDataException(
                            "Невозможно преобразовать в число ячейку [" + i + "][" + j + "]: " + array[i][j]
                    );
                }
            }
        }

        return sum;
    }
}
