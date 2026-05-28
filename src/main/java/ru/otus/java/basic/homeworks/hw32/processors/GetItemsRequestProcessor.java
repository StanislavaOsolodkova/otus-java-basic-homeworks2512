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

public class GetItemsRequestProcessor implements RequestProcessor {
    @Override
    public HttpResponse process(HttpRequest request) {
        List<Item> items = new ArrayList<>(Arrays.asList(
                new Item(1L, "Bread", 50),
                new Item(2L, "Milk", 150),
                new Item(3L, "Cheese", 400)
        ));
        HttpResponse response = new HttpResponse(200, "OK");
        response.setJsonBody(items);  // автоматически устанавливает Content-Type и тело
        return response;
    }
}
