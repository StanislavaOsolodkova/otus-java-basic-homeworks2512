package ru.otus.java.basic.homeworks.hw31.test;

import org.junit.jupiter.api.Test;
import ru.otus.java.basic.homeworks.hw31.main.ArrayUtils;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {

    // ---------- Тесты для getElementsAfterLastOne ----------

    @Test
    void shouldReturnTrailingElementsAfterLastOne() {
        int[] input = {1, 2, 1, 2, 2};
        int[] expected = {2, 2};
        assertArrayEquals(expected, ArrayUtils.getElementsAfterLastOne(input));
    }

    @Test
    void shouldReturnEmptyArrayWhenLastElementIsOne() {
        int[] input = {1, 2, 1};
        int[] expected = {};
        assertArrayEquals(expected, ArrayUtils.getElementsAfterLastOne(input));
    }

    @Test
    void shouldReturnEmptyArrayWhenOnlyOneIsLast() {
        int[] input = {2, 2, 1};
        int[] expected = {};
        assertArrayEquals(expected, ArrayUtils.getElementsAfterLastOne(input));
    }

    @Test
    void shouldReturnWholeArrayWhenOneIsFirst() {
        int[] input = {1, 3, 4, 5};
        int[] expected = {3, 4, 5};
        assertArrayEquals(expected, ArrayUtils.getElementsAfterLastOne(input));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenNoOne() {
        int[] input = {2, 2, 2, 2};
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ArrayUtils.getElementsAfterLastOne(input));
        assertEquals("Массив не содержит ни одной единицы", exception.getMessage());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenNullArray() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ArrayUtils.getElementsAfterLastOne(null));
        assertEquals("Входной массив не должен быть null", exception.getMessage());
    }

    // ---------- Тесты для checkArrayOnly1And2 ----------

    @Test
    void shouldReturnTrueWhenOnlyOnesAndTwosAndBothPresent() {
        int[] input = {1, 2};
        assertTrue(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnTrueForLongerValidArray() {
        int[] input = {1, 2, 2, 1};
        assertTrue(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnFalseWhenOnlyOnes() {
        int[] input = {1, 1};
        assertFalse(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnFalseWhenOnlyTwos() {
        int[] input = {2, 2, 2};
        assertFalse(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnFalseWhenContainsOtherNumbers() {
        int[] input = {1, 3};
        assertFalse(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnFalseWhenContainsOneTwoAndOther() {
        int[] input = {1, 2, 4};
        assertFalse(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnFalseWhenEmptyArray() {
        int[] input = {};
        assertFalse(ArrayUtils.checkArrayOnly1And2(input));
    }

    @Test
    void shouldReturnFalseWhenNullArray() {
        assertFalse(ArrayUtils.checkArrayOnly1And2(null));
    }
}