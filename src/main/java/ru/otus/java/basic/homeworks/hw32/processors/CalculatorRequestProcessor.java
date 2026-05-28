package ru.otus.java.basic.homeworks.hw32.processors;

import ru.otus.java.basic.homeworks.hw32.HttpRequest;
import ru.otus.java.basic.homeworks.hw32.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {
    @Override
    public HttpResponse process(HttpRequest request) {
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        String result = a + " + " + b + " = " + (a + b);
        HttpResponse response = new HttpResponse(200, "OK");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody("<html><body><h1>" + result + "</h1></body></html>");
        return response;
    }
}
