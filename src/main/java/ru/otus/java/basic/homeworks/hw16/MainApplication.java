package ru.otus.java.basic.homeworks.hw16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println("--- Тест 1: Создание списка от 1 до 10 ---");
        ArrayList<Integer> rangeList = OperationsList.createRangeList(1, 10);
        System.out.println("Список: " + rangeList);

        System.out.println("\n--- Тест 2: Сумма элементов > 5 ---");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int sumGreaterThanFive = OperationsList.sumElementsGreaterThanFive(numbers);
        System.out.println("Исходный список: " + numbers);
        System.out.println("Сумма элементов > 5: " + sumGreaterThanFive);

        System.out.println("\n--- Тест 3: Заполнение списка числом 99 ---");
        ArrayList<Integer> listToFill = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("До заполнения: " + listToFill);
        OperationsList.fillList(99, listToFill);
        System.out.println("После заполнения: " + listToFill);

        System.out.println("\n--- Тест 4: Увеличение элементов на 10 ---");
        ArrayList<Integer> listToIncrement = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("До увеличения: " + listToIncrement);
        OperationsList.incrementListElements(10, listToIncrement);
        System.out.println("После увеличения: " + listToIncrement);

        System.out.println("\n--- Тест 5: Работа со списком сотрудников ---");
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Иван", 25));
        employees.add(new Employee("Мария", 30));
        employees.add(new Employee("Петр", 28));
        employees.add(new Employee("Анна", 22));
        employees.add(new Employee("Сергей", 35));

        System.out.println("Список сотрудников:");
        for (Employee emp : employees) {
            System.out.println("  " + emp);
        }

        System.out.println("\n--- Тест 6: Список имен сотрудников ---");
        List<String> names = OperationsList.getEmployeeNames(employees);
        System.out.println("Имена: " + names);

        System.out.println("\n--- Тест 7: Сотрудники с возрастом >= 28 ---");
        List<Employee> filteredEmployees = OperationsList.filterEmployeesByAge(employees, 28);
        for (Employee emp : filteredEmployees) {
            System.out.println("  " + emp);
        }

        System.out.println("\n--- Тест 8: Проверка среднего возраста ---");
        boolean isAverageAbove25 = OperationsList.isAverageAgeGreaterThan(employees, 25);
        boolean isAverageAbove30 = OperationsList.isAverageAgeGreaterThan(employees, 30);
        System.out.println("Средний возраст > 25: " + isAverageAbove25);
        System.out.println("Средний возраст > 30: " + isAverageAbove30);

        System.out.println("\n--- Тест 9: Самый молодой сотрудник ---");
        Employee youngest = OperationsList.getYoungestEmployee(employees);
        if (youngest != null) {
            System.out.println("Самый молодой: " + youngest);
        }
    }
}
