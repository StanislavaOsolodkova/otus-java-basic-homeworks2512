package ru.otus.java.basic.homeworks.hw17;

import java.util.*;

public class PhoneBook {

        private final Map<String, List<String>> phoneBook;

        public PhoneBook() {
            this.phoneBook = new HashMap<>();
        }

        public void add(String name, String phone) {
            if (name == null || name.trim().isEmpty()) {
                return;
            }
            if (phone == null || phone.trim().isEmpty()) {
                return;
            }

            List<String> phones = phoneBook.get(name);
            if (phones == null) {
                phones = new ArrayList<>();
                phoneBook.put(name, phones);
            }
            phones.add(phone);
        }

        public List<String> find(String name) {
            if (name == null || name.trim().isEmpty()) {
                return new ArrayList<>();
            }

            List<String> phones = phoneBook.get(name);
            if (phones == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(phones);
        }

        public boolean containsPhoneNumber(String phone) {
            if (phone == null || phone.trim().isEmpty()) {
                return false;
            }

            for (List<String> phones : phoneBook.values()) {
                if (phones.contains(phone)) {
                    return true;
                }
            }
            return false;
        }
    }