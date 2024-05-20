package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;

public class EpicHandler extends Handler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String uriPath = exchange.getRequestURI().getPath();

        switch (method) {
            case "GET" -> handleGetRequest(exchange, uriPath, Epic.class);
            case "POST" -> handlePostRequest(exchange, uriPath, Epic.class);
            case "DELETE" -> handleDeleteRequest(exchange, uriPath, Epic.class);
            default -> sendText(exchange, 405, "Method Not Allowed");
        }
    }
}
