import historymanager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
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
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        var task1 = new Task("AAA", "AAA", Status.NEW);
        var task2 = new Task("BBB", "BBB", Status.IN_PROGRESS);
        var task3 = new Task("CCC", "CCC", Status.DONE);
        var task4 = new Task("DDD", "DDD", Status.DONE);
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
}
