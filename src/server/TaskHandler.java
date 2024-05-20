package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends Handler {
    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String uriPath = exchange.getRequestURI().getPath();
        System.out.println("Handling request: " + method + " " + uriPath);

        switch (method) {
            case "GET" -> handleGetRequest(exchange, uriPath, Task.class);
            case "POST" -> handlePostRequest(exchange, uriPath, Task.class);
            case "DELETE" -> handleDeleteRequest(exchange, uriPath, Task.class);
            default -> sendText(exchange, 405, "Method Not Allowed");
        }
    }
}
