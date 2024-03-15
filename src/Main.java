import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("English course", "Do homework", Status.IN_PROGRESS);
        Task task2 = new Task("Math course", "Study integrals", Status.NEW);
        Epic epic = new Epic("Java project", "Create money printing machine using Java", Status.NEW);
        SubTask sub1 = new SubTask("Create method one", "Create money printing method 1", Status.NEW, epic);
        SubTask sub2 = new SubTask("Create method two", "Create money printing method 2", Status.NEW, epic);
        TaskManager.addTask(epic);
        HashMap<Integer, SubTask> subtasks = epic.getSubTasks();

        TaskManager.addTask(task1);
        TaskManager.addTask(task2);

        TaskManager.printTasks();
        System.out.println(" ");

        sub1 =  new SubTask("Create method one", sub1.getId(), "Create money printing method 1", Status.DONE, epic);
        sub2 =  new SubTask("Create method two", sub2.getId(),"Create money printing method 2", Status.DONE, epic);
        epic.updateSubTaskById(sub1.getId(), sub1);
        epic.updateSubTaskById(sub2.getId(), sub2);

        TaskManager.printTasks();
    }
}
