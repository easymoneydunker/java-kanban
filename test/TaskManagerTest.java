import manager.ManagerRuntimeException;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {
    static final Epic epic = new Epic("Epic", 1, "Epic", Status.IN_PROGRESS, LocalDateTime.now().plusDays(3), Duration.ofMinutes(600));
    static final Task task = new Task("Task1", 123, "Task1", Status.NEW, LocalDateTime.now().plusDays(5), Duration.ofMinutes(9000));
    static final SubTask subTask = new SubTask("Sub", 12345, "Sub1", Status.DONE, epic, LocalDateTime.now().plusDays(1), Duration.ofMinutes(400));
    static final SubTask subTask2 = new SubTask("Sub2", 123465, "Sub2", Status.DONE, epic, LocalDateTime.now().plusDays(2), Duration.ofMinutes(800));
    Task task1 = new Task("AAA", "AAA", Status.NEW);
    Task task2 = new Task("BBB", "BBB", Status.IN_PROGRESS);
    Task task3 = new Task("CCC", "CCC", Status.DONE);
    Epic task4 = new Epic("Epic", 12345, "epic", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(60));
    static Path testFile;

    @Test
    void testOverlappingSubTasks() {
        Epic epic = new Epic("Epic 3", "Description", Status.NEW);
        SubTask subTask1 = new SubTask("SubTask 1", 12345678, "Description", Status.NEW, epic, LocalDateTime.now(), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("SubTask 2", 12345679, "Description", Status.DONE, epic, LocalDateTime.now().plusMinutes(30), Duration.ofHours(2));
        Assertions.assertThrows(ManagerRuntimeException.class, () -> {
            epic.addSubTask(subTask1);
            epic.addSubTask(subTask2);
        });
    }
}
