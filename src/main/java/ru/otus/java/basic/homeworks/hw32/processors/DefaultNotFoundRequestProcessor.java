package ru.otus.java.basic.homeworks.hw32.processors;

import ru.otus.java.basic.homeworks.hw32.HttpRequest;
import ru.otus.java.basic.homeworks.hw32.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotFoundRequestProcessor implements RequestProcessor {
    @Override
    public HttpResponse process(HttpRequest request) {
        HttpResponse response = new HttpResponse(404, "Not Found");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody("<html><body><h1>404.. Page Not Found</h1></body></html>");
        return response;
    }
}
