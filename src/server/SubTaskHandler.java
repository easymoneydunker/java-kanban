package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.SubTask;

import java.io.IOException;

public class SubTaskHandler extends Handler {

    public SubTaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String uriPath = exchange.getRequestURI().getPath();

        switch (method) {
            case "GET" -> handleGetRequest(exchange, uriPath, SubTask.class);
            case "POST" -> handlePostRequest(exchange, uriPath, SubTask.class);
            case "DELETE" -> handleDeleteRequest(exchange, uriPath, SubTask.class);
            default -> sendText(exchange, 405, "Method Not Allowed");
        }
    }
}
