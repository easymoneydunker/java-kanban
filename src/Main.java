import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("English course", "Do homework", Status.IN_PROGRESS);
        Task task2 = new Task("Math course", "Study integrals", Status.NEW);
        Epic epic = new Epic("Java project", "Create money printing machine using Java", Status.NEW);
        SubTask sub1 = new SubTask("Create method one", "Create money printing method 1", Status.NEW, epic);
        SubTask sub2 = new SubTask("Create method two", "Create money printing method 2", Status.NEW, epic);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(epic);
        taskManager.addSubTaskToEpic(epic, sub1);
        taskManager.addSubTaskToEpic(epic, sub2);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
        System.out.println();

        sub1 = new SubTask("Create method one", sub1.getId(), "Create money printing method 1", Status.DONE, epic);
        sub2 = new SubTask("Create method two", sub2.getId(), "Create money printing method 2", Status.DONE, epic);
        taskManager.updateSubTask(sub1, epic);
        taskManager.updateSubTask(sub2, epic);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
    }
}
