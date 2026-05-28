package ru.otus.java.basic.homeworks.hw32;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusText;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
        setHeader("Content-Length", String.valueOf(body.length));
    }

    public void setBody(String body) {
        setBody(body.getBytes(StandardCharsets.UTF_8));
    }

    public void setJsonBody(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        setHeader("Content-Type", "application/json; charset=utf-8");
        setBody(json);
    }

    public void write(OutputStream output) throws IOException {
        StringBuilder responseLine = new StringBuilder();
        responseLine.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(statusText)
                .append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            responseLine.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }
        responseLine.append("\r\n");
        output.write(responseLine.toString().getBytes(StandardCharsets.UTF_8));
        if (body != null) {
            output.write(body);
        }
    }
}
