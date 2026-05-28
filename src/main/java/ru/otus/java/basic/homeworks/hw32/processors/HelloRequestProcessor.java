package ru.otus.java.basic.homeworks.hw32.processors;

import ru.otus.java.basic.homeworks.hw32.HttpRequest;
import ru.otus.java.basic.homeworks.hw32.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloRequestProcessor implements RequestProcessor {
    @Override
    public HttpResponse process(HttpRequest request) {
        HttpResponse response = new HttpResponse(200, "OK");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody("<html><body><h1>Hello World!!!</h1></body></html>");
        return response;
    }
}
