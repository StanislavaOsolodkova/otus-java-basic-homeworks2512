package ru.otus.java.basic.homeworks.hw32.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.homeworks.hw32.HttpRequest;
import ru.otus.java.basic.homeworks.hw32.app.Item;
import ru.otus.java.basic.homeworks.hw32.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateItemRequestProcessor implements RequestProcessor {
    @Override
    public HttpResponse process(HttpRequest request) {
        Gson gson = new Gson();
        Item item = gson.fromJson(request.getBody(), Item.class);
        System.out.println("Создан объект: " + item);
        // Возвращаем 201 Created с JSON-подтверждением
        HttpResponse response = new HttpResponse(201, "Created");
        response.setJsonBody(item);  // можно вернуть созданный объект
        return response;
    }
}
