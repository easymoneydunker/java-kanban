import historymanager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

public class InMemoryHistoryManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryHistoryManager inMemoryHistoryManager;
    InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void historyManagerShouldReturnLastAddedElement() {
        inMemoryHistoryManager.add(new Task("AAA", "AAA", Status.NEW));
        inMemoryHistoryManager.add(new Task("BBB", "BBB", Status.NEW));
        var expected = new Task("BBB", "BBB", Status.NEW);
        Assertions.assertEquals(expected, inMemoryHistoryManager.getLastTask());
    }

    /**
     * Add four elements, then delete one
     */
    @Test
    void historyManagerSizeShouldBeThreeAfterAddingThreeElements() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.remove(task3);
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void historyShouldBeEmptyWithoutAddingTasks() {
        Assertions.assertEquals(0, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void historyManagerDoesNotDuplicateTasks() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }
}
