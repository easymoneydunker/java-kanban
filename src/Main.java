import historyManager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    public static void main(String[] args) {
        Epic epic = new Epic("epic1", "epic1", Status.NEW);
        SubTask sub1 = new SubTask("sub1", "sub1", Status.NEW, epic);
        SubTask sub2 = new SubTask("sub2", "sub2", Status.NEW, epic);
        SubTask sub3 = new SubTask("sub3", "sub3", Status.NEW, epic);
        Epic epic2 = new Epic("empty epic", "epic1", Status.NEW);

        inMemoryTaskManager.addTask(epic);
        inMemoryTaskManager.addTask(sub1);
        inMemoryTaskManager.addTask(sub2);
        inMemoryTaskManager.addTask(sub3);
        inMemoryTaskManager.addTask(epic2);

        inMemoryTaskManager.getEpicById(epic.getId());
        inMemoryTaskManager.getEpicById(epic2.getId());
        inMemoryTaskManager.getSubTaskById(sub3.getId());
        inMemoryTaskManager.getSubTaskById(sub2.getId());
        inMemoryTaskManager.getSubTaskById(sub1.getId());
        inMemoryTaskManager.getEpicById(epic.getId());

        System.out.println(inMemoryTaskManager.getHistory().size());

        inMemoryTaskManager.deleteEpicById(epic.getId());

        System.out.println(inMemoryTaskManager.getHistory().size());
    }
}
