package server;

import com.sun.net.httpserver.HttpServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    static TaskManager manager;
    HttpServer server;

    public HttpTaskServer() {
        manager = new InMemoryTaskManager();
    }

    public HttpTaskServer(TaskManager manager1) {
        manager = manager1;
    }

    public static void main(String[] args) throws IOException {
        manager = new InMemoryTaskManager();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubTaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.start();
        System.out.println("Server started on port " + server.getAddress().getPort());
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubTaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.start();
        System.out.println("Server started on port " + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
    }
}
