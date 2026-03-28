package ru.otus.java.basic.homeworks.hw16;

import java.util.ArrayList;
import java.util.List;

public class OperationsList {
    public static ArrayList<Integer> createRangeList(int min, int max) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }
        return list;
    }

    public static int sumElementsGreaterThanFive(List<Integer> list) {
        if (list == null) {
            return 0;
        }

        int sum = 0;
        for (Integer num : list) {
            if (num != null && num > 5) {
                sum += num;
            }
        }
        return sum;
    }

    public static void fillList(int number, List<Integer> list) {
        if (list == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, number);
        }
    }

    public static void incrementListElements(int number, List<Integer> list) {
        if (list == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                list.set(i, list.get(i) + number);
            }
        }
    }

    public static List<String> getEmployeeNames(List<Employee> employees) {
        List<String> names = new ArrayList<>();

        if (employees == null) {
            return names;
        }

        for (Employee emp : employees) {
            if (emp != null && emp.getName() != null) {
                names.add(emp.getName());
            }
        }

        return names;
    }

    public static List<Employee> filterEmployeesByAge(List<Employee> employees, int minAge) {
        List<Employee> filtered = new ArrayList<>();

        if (employees == null) {
            return filtered;
        }

        for (Employee emp : employees) {
            if (emp != null && emp.getAge() >= minAge) {
                filtered.add(emp);
            }
        }

        return filtered;
    }

    public static boolean isAverageAgeGreaterThan(List<Employee> employees, double minAverageAge) {
        if (employees == null || employees.isEmpty()) {
            return false;
        }

        int totalAge = 0;
        int count = 0;

        for (Employee emp : employees) {
            if (emp != null) {
                totalAge += emp.getAge();
                count++;
            }
        }

        if (count == 0) {
            return false;
        }

        double averageAge = (double) totalAge / count;
        return averageAge > minAverageAge;
    }

    public static Employee getYoungestEmployee(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return null;
        }

        Employee youngest = null;

        for (Employee emp : employees) {
            if (emp != null) {
                if (youngest == null || emp.getAge() < youngest.getAge()) {
                    youngest = emp;
                }
            }
        }

        return youngest;
    }
}
