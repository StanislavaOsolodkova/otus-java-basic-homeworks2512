package ru.otus.java.basic.homeworks.chat.server;

import java.util.Set;

public class ProfanityFilter {
    private static final Set<String> BAD_WORDS = Set.of("дурак", "дура");
    private static final String REPLACEMENT = "***";

    public String filter(String text) {
        String result = text;
        for (String word : BAD_WORDS) {
            result = result.replaceAll("(?i)\\b" + word + "\\b", REPLACEMENT);
        }
        return result;
    }
}
