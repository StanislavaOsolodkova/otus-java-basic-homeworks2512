package ru.otus.java.basic.homeworks.hw15;

public class ArraySumCalculator {
    private static final int EXPECTED_SIZE = 4;

    public static int sumArrayElements(String[][] array)
            throws AppArraySizeException, AppArrayDataException {

        if (array == null) {
            throw new AppArraySizeException(
                    "Массив не может быть null. Ожидаемый размер: " + EXPECTED_SIZE + "x" + EXPECTED_SIZE
            );
        }

        if (array.length != EXPECTED_SIZE) {
            throw new AppArraySizeException(
                    "Неверное количество строк. Ожидалось: " + EXPECTED_SIZE +
                            ", получено: " + array.length
            );
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new AppArraySizeException(
                        "Строка " + i + " равна null. Ожидаемый размер: " + EXPECTED_SIZE + "x" + EXPECTED_SIZE
                );
            }

            if (array[i].length != EXPECTED_SIZE) {
                throw new AppArraySizeException(
                        "Неверное количество столбцов в строке " + i +
                                ". Ожидалось: " + EXPECTED_SIZE +
                                ", получено: " + array[i].length
                );
            }
        }

        int sum = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                } catch (NumberFormatException e) {
                    throw new AppArrayDataException(
                            "Невозможно преобразовать в число ячейку [" + i + "][" + j +
                                    "]: \"" + array[i][j] + "\""
                    );
                }
            }
        }

        return sum;
    }
}
