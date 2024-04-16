import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

public class InMemoryTaskManagerTest {
    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void taskKeySetSizeShouldRemainTheSameAfterUpdatingTask() {
        inMemoryTaskManager.addTask(new Task("Task1", 123, "Task1", Status.NEW));
        inMemoryTaskManager.updateTask(new Task("Task1", 123, "Task1", Status.DONE));
        Assertions.assertEquals(1, inMemoryTaskManager.getTasks().keySet().size());
    }

    @Test
    public void taskValuesListSizeShouldRemainTheSameAfterUpdatingTask() {
        inMemoryTaskManager.addTask(new Task("Task1", 123, "Task1", Status.NEW));
        inMemoryTaskManager.updateTask(new Task("Task1", 123, "Task1", Status.DONE));
        Assertions.assertEquals(1, inMemoryTaskManager.getTasks().values().size());
    }

    @Test
    public void SubTaskKeySetSizeShouldRemainTheSameAfterUpdatingTask() {
        Epic epic = new Epic("task.Epic", "task.Epic", Status.NEW);
        inMemoryTaskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", "Sub", Status.NEW, epic);
        inMemoryTaskManager.addTask(subTask);
        inMemoryTaskManager.updateEpicSubTask(epic, new SubTask("Sub", "Sub1", Status.DONE, epic));
        Assertions.assertEquals(1, inMemoryTaskManager.getSubTasks().keySet().size());
    }

    @Test
    public void SubTaskValueListSizeShouldRemainTheSameAfterUpdatingTask() {
        Epic epic = new Epic("task.Epic", "task.Epic", Status.NEW);
        inMemoryTaskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", "Sub", Status.NEW, epic);
        inMemoryTaskManager.addTask(subTask);
        inMemoryTaskManager.updateEpicSubTask(epic, new SubTask("Sub", "Sub1", Status.DONE, epic));
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
