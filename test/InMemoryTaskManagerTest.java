import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest  extends TaskManagerTest<InMemoryTaskManager>  {
    static InMemoryTaskManager inMemoryTaskManager;
    static LocalDateTime startTime;
    static Duration duration;

    @BeforeAll
    static void beforeAll() {
        startTime = LocalDateTime.now();
        duration = Duration.ofDays(2);
    }

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void taskKeySetSizeShouldRemainTheSameAfterUpdatingTask() {
        inMemoryTaskManager.addTask(new Task("Task1", 123, "Task1", Status.NEW, startTime, duration));
        inMemoryTaskManager.updateTask(new Task("Task1", 123, "Task1", Status.DONE, startTime.plusDays(5), duration.minusHours(3)));
        Assertions.assertEquals(1, inMemoryTaskManager.getTasks().keySet().size());
    }

    @Test
    public void taskValuesListSizeShouldRemainTheSameAfterUpdatingTask() {
        inMemoryTaskManager.addTask(new Task("Task1", 123, "Task1", Status.NEW, startTime, duration));
        inMemoryTaskManager.updateTask(new Task("Task1", 123, "Task1", Status.DONE, startTime.plusDays(2), duration.minusHours(3)));
        Assertions.assertEquals(1, inMemoryTaskManager.getTasks().values().size());
    }

    @Test
    public void SubTaskKeySetSizeShouldRemainTheSameAfterUpdatingTask() {
        Epic epic = new Epic("task.Epic", "task.Epic", Status.NEW);
        inMemoryTaskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", 123456,"Sub", Status.NEW, epic, LocalDateTime.now(), Duration.ofMinutes(60));
        inMemoryTaskManager.addTask(subTask);
        inMemoryTaskManager.updateSubTask(new SubTask("Updated", 123456,"Sub", Status.NEW, epic, LocalDateTime.now(), Duration.ofMinutes(60)), epic);
        Assertions.assertEquals(1, inMemoryTaskManager.getSubTasks().keySet().size());
    }

    @Test
    public void SubTaskValueListSizeShouldRemainTheSameAfterUpdatingTask() {
        Epic epic = new Epic("task.Epic", "task.Epic", Status.NEW);
        inMemoryTaskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", 123456,"Sub", Status.NEW, epic, LocalDateTime.now(), Duration.ofMinutes(60));
        inMemoryTaskManager.addTask(subTask);
        inMemoryTaskManager.updateSubTask(new SubTask("Updated", 123456,"Sub", Status.IN_PROGRESS, epic, LocalDateTime.now(), Duration.ofMinutes(60)), epic);
        Assertions.assertEquals(1, inMemoryTaskManager.getSubTasks().values().size());
    }


    @Test
    public void SubTasksShouldBeDeletedOnEpicRemove() {
        Epic epic = new Epic("task.Epic", "task.Epic", Status.NEW);
        inMemoryTaskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", "Sub", Status.NEW, epic);
        inMemoryTaskManager.addTask(subTask);
        SubTask subTask1 = new SubTask("Sub1", "Sub", Status.NEW, epic);
        inMemoryTaskManager.addTask(subTask1);
        inMemoryTaskManager.deleteEpicById(epic.getId());
        Assertions.assertEquals(0, inMemoryTaskManager.getSubTasks().size());
    }

}
