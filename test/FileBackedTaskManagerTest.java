import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    static FileBackedTaskManager manager;

    @BeforeAll
    static void beforeAll() throws IOException {
        testFile = Files.createTempFile("test", "test");
        manager = new FileBackedTaskManager(testFile);
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(subTask);
        manager.addTask(subTask2);
        manager.save(testFile);
    }

    @Test
    void fileBackedTaskManagerShouldAddRecordsToFile() throws IOException {
        Assertions.assertEquals(4, manager.getTasks().size() + manager.getSubTasks().size() + manager.getEpics().size());
    }

    @Test
    void addedTaskToFileShoulBeEqualToExpectedOne() {
        Assertions.assertEquals(epic, manager.getEpics().get(1));
    }

    @Test
    void loadFromFileCorrectlyLoadsDataFromFile() {
        int expected = manager.getTasks().size() + manager.getSubTasks().size() + manager.getEpics().size();
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(testFile.toFile());
        fileBackedTaskManager.save(Path.of("src/data.csv"));
        int actual = fileBackedTaskManager.getTasks().size() + fileBackedTaskManager.getSubTasks().size() + fileBackedTaskManager.getEpics().size();
        Assertions.assertEquals(expected, actual);
    }


}
