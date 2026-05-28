package ru.otus.java.basic.homeworks.hw32.processors;

import ru.otus.java.basic.homeworks.hw32.HttpRequest;
import ru.otus.java.basic.homeworks.hw32.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    HttpResponse process(HttpRequest request) throws IOException;
}
