import manager.InMemoryTaskManager;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("English course", "Do homework", Status.IN_PROGRESS);
        Task task2 = new Task("Math course", "Study integrals", Status.NEW);
        Epic epic = new Epic("Java project", "Create money printing machine using Java", Status.NEW);
        SubTask sub1 = new SubTask("Create method one", "Create money printing method 1", Status.NEW, epic);
        SubTask sub2 = new SubTask("Create method two", "Create money printing method 2", Status.NEW, epic);

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(epic);
        inMemoryTaskManager.addSubTaskToEpic(epic, sub1);
        inMemoryTaskManager.addSubTaskToEpic(epic, sub2);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());
        System.out.println();

        sub1 = new SubTask("Create method one", sub1.getId(), "Create money printing method 1", Status.DONE, epic);
        sub2 = new SubTask("Create method two", sub2.getId(), "Create money printing method 2", Status.DONE, epic);
        inMemoryTaskManager.updateSubTask(sub1, epic);
        inMemoryTaskManager.updateSubTask(sub2, epic);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());
    }
}
