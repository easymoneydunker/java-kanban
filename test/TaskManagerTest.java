import org.junit.jupiter.api.*;
public class TaskManagerTest {
    static TaskManager taskManager;
    @BeforeAll
    static void beforeAll() {
        taskManager = new TaskManager();
    }
    @Test
    public void taskKeySetSizeShouldRemainTheSameAfterUpdatingTask() {
        taskManager = new TaskManager();
        taskManager.addTask(new Task("Task1", 123, "Task1", Status.NEW));
        taskManager.updateTaskById(taskManager.getTasks().get(123).getId(),
                new Task("Task1", 123, "Task1", Status.DONE));
        Assertions.assertEquals(1, taskManager.getTasks().keySet().size());
    }
    @Test
    public void taskValuesListSizeShouldRemainTheSameAfterUpdatingTask() {
        taskManager = new TaskManager();
        taskManager.addTask(new Task("Task1", 123, "Task1", Status.NEW));
        taskManager.updateTaskById(taskManager.getTasks().get(123).getId(),
                new Task("Task1", 123, "Task1", Status.DONE));
        Assertions.assertEquals(1, taskManager.getTasks().values().size());
    }

    @Test
    public void SubTaskKeySetSizeShouldRemainTheSameAfterUpdatingTask() {
        Epic epic = new Epic("Epic", "Epic", Status.NEW);
        taskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", "Sub", Status.NEW, epic);
        taskManager.addTask(subTask);
        taskManager.updateEpicSubTaskBySubTaskId(epic, subTask.getId(), new SubTask("Sub", "Sub1", Status.DONE, epic));
        Assertions.assertEquals(1, taskManager.getSubTasks().keySet().size());
    }

    @Test
    public void SubTaskValueListSizeShouldRemainTheSameAfterUpdatingTask() {
        Epic epic = new Epic("Epic", "Epic", Status.NEW);
        taskManager.addTask(epic);
        SubTask subTask = new SubTask("Sub", "Sub", Status.NEW, epic);
        taskManager.addTask(subTask);
        taskManager.updateEpicSubTaskBySubTaskId(epic, subTask.getId(), new SubTask("Sub", "Sub1", Status.DONE, epic));
        Assertions.assertEquals(1, taskManager.getSubTasks().values().size());
    }

}
