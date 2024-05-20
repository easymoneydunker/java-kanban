package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.ManagerRuntimeException;
import manager.TaskManager;
import manager.TaskOverlappingException;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

public abstract class Handler implements HttpHandler {
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    protected TaskManager manager;
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public Handler(TaskManager manager) {
        this.manager = manager;
    }

    protected Status getStatusFromString(String string) {
        return switch (string) {
            case "NEW" -> Status.NEW;
            case "DONE" -> Status.DONE;
            case "IN_PROGRESS" -> Status.IN_PROGRESS;
            default -> throw new IllegalArgumentException("Unrecognized status: " + string);
        };
    }

    protected int getIdFromURI(String uriPath) {
        String[] params = uriPath.split("/");
        if (params.length >= 3) {
            try {
                return Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    protected void sendText(HttpExchange exchange, int code, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            System.out.println("Response sent with status code: " + code);
        }
    }

    protected void handleGetRequest(HttpExchange exchange, String uriPath, Class<?> objectClass) throws IOException {
        String type = switch (objectClass.getName()) {
            case "task.Task" -> "Task";
            case "task.Epic" -> "Epic";
            case "task.SubTask" -> "SubTask";
            default -> throw new RuntimeException("No such task type");
        };

        System.out.println("URI Path: " + uriPath);
        System.out.println("Type: " + type);

        if (uriPath.equalsIgnoreCase("/" + type.toLowerCase() + "s/")) {
            String json = switch (type) {
                case "Task" -> gson.toJson(manager.getTasks(), new TypeToken<HashMap<Integer, Task>>() {}.getType());
                case "Epic" -> gson.toJson(manager.getEpics(), new TypeToken<HashMap<Integer, Epic>>() {}.getType());
                case "SubTask" -> gson.toJson(manager.getSubTasks(), new TypeToken<HashMap<Integer, SubTask>>() {}.getType());
                default -> throw new RuntimeException("Unknown type: " + type);
            };
            sendText(exchange, 200, json);
            return;
        }

        int id = getIdFromURI(uriPath);
        if (id < 0 && id != -1) {
            sendText(exchange, 400, "Invalid " + type + " ID format");
            return;
        }

        System.out.println("ID: " + id);

        Object task = switch (type) {
            case "Task" -> manager.getTaskById(id).orElse(null);
            case "Epic" -> manager.getEpicById(id).orElse(null);
            case "SubTask" -> manager.getSubTaskById(id).orElse(null);
            default -> null;
        };

        if (task != null) {
            String response = gson.toJson(task);
            sendText(exchange, 200, response);
        } else {
            sendText(exchange, 404, type + " not found");
        }
    }

    protected void handlePostRequest(HttpExchange exchange, String uriPath, Class<?> objectClass) throws IOException {
        String type;
        switch (objectClass.getName()) {
            case "task.Task" -> type = "Task";
            case "task.Epic" -> type = "Epic";
            case "task.SubTask" -> type = "SubTask";
            default -> throw new RuntimeException("No such task type");
        }

        try (InputStream inputStream = exchange.getRequestBody()) {
            String input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String statusString = jsonObject.get("status").getAsString();
            Status status = getStatusFromString(statusString);
            LocalDateTime startTime = jsonObject.has("startTime") ? LocalDateTime.parse(jsonObject.get("startTime").getAsString(), dateTimeFormatter) : null;
            Duration duration = jsonObject.has("duration") ? Duration.ofSeconds(jsonObject.get("duration").getAsLong()) : null;

            int id = getIdFromURI(uriPath);
            if (id < 0 && id != -1) {
                sendText(exchange, 400, "Invalid " + type + " ID format");
                return;
            }

            switch (type) {
                case "Task" -> {
                    Task task = id > -1 ? new Task(name, id, description, status, startTime, duration) : new Task(name, description, status, startTime, duration);
                    if (id > -1) {
                        manager.updateTask(task);
                        sendText(exchange, 201, "Task Updated");
                    } else {
                        manager.addTask(task);
                        sendText(exchange, 201, "Task Created");
                    }
                }
                case "Epic" -> {
                    Epic epic = id > -1 ? new Epic(name, id, description, status, startTime, duration) : new Epic(name, description, status, startTime, duration);
                    if (id > -1) {
                        manager.updateEpic(epic);
                        sendText(exchange, 201, "Epic Updated");
                    } else {
                        manager.addTask(epic);
                        sendText(exchange, 201, "Epic Created");
                    }
                }
                case "SubTask" -> {
                    int epicId = jsonObject.get("epic").getAsInt();
                    Optional<Epic> epicOptional = manager.getEpicById(epicId);
                    Epic epic = epicOptional.orElseThrow(() -> new RuntimeException("Epic not found for epic id: " + epicId));
                    SubTask subTask = id > -1 ? new SubTask(name, id, description, status, epic, startTime, duration) : new SubTask(name, description, status, epic, startTime, duration);
                    if (subTask.getId() > -1) {
                        manager.addSubTaskToEpic(manager.getEpicById(epicId).orElseThrow(() -> new ManagerRuntimeException("Epic not found")), subTask);
                        sendText(exchange, 201, "SubTask Updated");
                    } else {
                        manager.addTask(subTask);
                        sendText(exchange, 201, "SubTask Created");
                    }
                }
            }
        } catch (TaskOverlappingException e) {
            sendText(exchange, 406, "Error. " + type + " overlapping with existing");
        }
        catch (ManagerRuntimeException e) {
            sendText(exchange, 404, "Error. " + type + " not found for");
        } catch (Exception e) {
            sendText(exchange, 500, e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    protected void handleDeleteRequest(HttpExchange exchange, String uriPath, Class<?> objectClass) throws IOException {
        String type;
        switch (objectClass.getName()) {
            case "task.Task" -> type = "Task";
            case "task.Epic" -> type = "Epic";
            case "task.SubTask" -> type = "SubTask";
            default -> throw new RuntimeException("No such task type");
        }

        int id = getIdFromURI(uriPath);
        if (id < 0) {
            sendText(exchange, 400, "Invalid " + type + " ID format");
            return;
        }

        boolean deleted = false;
        switch (type) {
            case "Task" -> {
                if (manager.getTaskById(id).isPresent()) {
                    manager.deleteTaskById(id);
                    deleted = true;
                }
            }
            case "Epic" -> {
                if (manager.getEpicById(id).isPresent()) {
                    manager.deleteEpicById(id);
                    deleted = true;
                }
            }
            case "SubTask" -> {
                if (manager.getSubTaskById(id).isPresent()) {
                    manager.deleteSubTaskById(id);
                    deleted = true;
                }
            }
        }

        if (deleted) {
            sendText(exchange, 200, type + " deleted");
        } else {
            sendText(exchange, 404, type + " not found");
        }
    }
}

