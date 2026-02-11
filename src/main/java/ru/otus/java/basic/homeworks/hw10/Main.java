package ru.otus.java.basic.homeworks.hw10;

public class Main {
    public static void main(String[] args) {
        User[] users = new User[10];

        users[0] = new User("Иванов", "Иван", "Иванович", 1965, "ivanov@mail.ru");
        users[1] = new User("Петров", "Петр", "Петрович", 1990, "petrov@gmail.com");
        users[2] = new User("Сидорова", "Анна", "Владимировна", 1975, "sidorova@yandex.ru");
        users[3] = new User("Кузнецов", "Алексей", "Сергеевич", 2000, "kuznetsov@mail.ru");
        users[4] = new User("Смирнова", "Ольга", "Игоревна", 1982, "smirnova@gmail.com");
        users[5] = new User("Васильев", "Дмитрий", "Александрович", 1958, "vasilyev@mail.ru");
        users[6] = new User("Николаева", "Елена", null, 1995, "nikolaeva@yandex.ru");
        users[7] = new User("Федоров", "Сергей", "Витальевич", 1970, "fedorov@gmail.com");
        users[8] = new User("Александрова", "Мария", "Дмитриевна", 2005, "alexandrova@mail.ru");
        users[9] = new User("Павлов", "Андрей", "Олегович", 1960, "pavlov@yandex.ru");

        System.out.println("=== ПОЛЬЗОВАТЕЛИ СТАРШЕ 40 ЛЕТ ===\n");

        int currentYear = java.time.Year.now().getValue();

        for (int i = 0; i < users.length; i++) {
            User currentUser = users[i];
            int age = currentYear - currentUser.getBirthYear();
            if (age > 40) {
                currentUser.printInfo();
                System.out.println();
            }
        }
    }
}