package ru.otus.java.basic.homeworks.hw17;

public class MainApplication {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Иванов", "+7(495)123-45-67");
        phoneBook.add("Иванов", "+7(495)123-45-68"); // Второй номер
        phoneBook.add("Петров", "+7(495)987-65-43");
        phoneBook.add("Иванов", "+7(495)123-45-67"); // Дубликат

        System.out.println("Номера Иванова: " + phoneBook.find("Иванов"));
        System.out.println("Номера Петрова: " + phoneBook.find("Петров"));
        System.out.println("Номера Сидорова: " + phoneBook.find("Сидоров"));

        System.out.println("Телефон +7(495)123-45-67 существует: " +
                phoneBook.containsPhoneNumber("+7(495)123-45-67"));
        System.out.println("Телефон +7(495)999-99-99 существует: " +
                phoneBook.containsPhoneNumber("+7(495)999-99-99"));
    }
}
