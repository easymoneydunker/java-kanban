import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest {
    static Path testFile;
    private final Epic epic = new Epic("Epic", 1, "Epic", Status.IN_PROGRESS);
    private final Task task = new Task("Task", 2, "Task", Status.IN_PROGRESS);
    private final SubTask subTask = new SubTask("Sub", 3, "Sub", Status.NEW, epic);
    FileBackedTaskManager manager;

    @BeforeEach
    void beforeEach() throws IOException {
        testFile = Files.createTempFile("test", "test");
        manager = new FileBackedTaskManager(testFile);
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(subTask);
    }

    @Test
    void fileBackedTaskManagerShouldAddRecordsToFile() throws IOException {
        Assertions.assertEquals(3, manager.getTasks().size() + manager.getSubTasks().size() + manager.getEpics().size());
    }

    @Test
    void addedTaskToFileShoulBeEqualToExpectedOne() {
        Assertions.assertEquals(epic, manager.getEpics().get(1));
    }

    @Test
    void loadFromFileCorrectlyLoadsDataFromFile() {
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(new File(testFile.toString()));
        assert newManager != null;
        int expected = manager.getTasks().size() + manager.getSubTasks().size() + manager.getEpics().size();
        int actual = newManager.getTasks().size() + newManager.getSubTasks().size() + newManager.getEpics().size();
        Assertions.assertEquals(expected, actual);
    }
}
