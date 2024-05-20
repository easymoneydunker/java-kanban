import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import server.EpicAdapter;
import server.HttpTaskServer;
import server.SubTaskAdapter;
import server.TaskAdapter;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.clearAllEpics();
        manager.clearAllTasks();
        manager.clearAllSubTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", 1234567 ,"Testing task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks().values().stream().toList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpicAndSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2" ,"Testing task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpics = URI.create("http://localhost:8080/epics/");
        URI urlSubTasks = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpics).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epics = manager.getEpics().values().stream().toList();
        System.out.println(manager.getEpics());

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Некорректное количество задач");
        assertEquals("Test 2", epics.get(0).getName(), "Некорректное имя задачи");

        epic = manager.getEpics().values().stream().findAny().orElseThrow(() -> new RuntimeException("No epics"));

        SubTask subTask = new SubTask("Sub test", 0 ,"Testing task 2", Status.NEW, epic, LocalDateTime.now().plusDays(10), Duration.ofMinutes(20));
        taskJson = gson.toJson(subTask);

        request = HttpRequest.newBuilder().uri(urlSubTasks).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<SubTask> subTasks = manager.getSubTasks().values().stream().toList();

        assertNotNull(subTasks, "Задачи не возвращаются");
        assertEquals(1, subTasks.size(), "Некорректное количество задач");
        assertEquals("Sub test", subTasks.get(0).getName(), "Некорректное имя задачи");
    }
}