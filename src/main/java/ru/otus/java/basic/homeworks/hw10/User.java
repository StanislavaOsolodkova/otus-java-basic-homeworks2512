package ru.otus.java.basic.homeworks.hw10;

public class User {
    private String lastName;
    private String firstName;
    private String middleName;
    private int birthYear;
    private String email;


    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getEmail() {
        return email;
    }

    public User(String lastName, String firstName, String middleName, int birthYear, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthYear = birthYear;
        this.email = email;
    }

    public void printInfo() {
        System.out.println("ФИО: " + lastName + ' ' + firstName + ' ' + middleName);
        System.out.println("Год рождения: " + birthYear);
        System.out.println("e-mail: " + email);
    }
}