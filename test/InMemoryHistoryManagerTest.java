import historyManager.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    public void addMethodShouldAddTasksToTaskHistory() {
        Task task = new Task("task", "task", Status.NEW);
        Epic epic = new Epic("epic", "epic", Status.NEW);
        SubTask subTask = new SubTask("sub", "sub", Status.NEW, epic);

        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subTask);

        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void historyListSizeShouldBeLessThenEleven() {
        for (int i = 0; i < 15; i++) {
            inMemoryHistoryManager.add(new Task("task", "task", Status.NEW));
        }
        Assertions.assertEquals(10, inMemoryHistoryManager.getHistory().size());
    }
}
