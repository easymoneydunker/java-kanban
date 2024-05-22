package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public interface TaskManager {
    void addTask(Task task);

    void addTask(Epic epic);

    void addTask(SubTask subTask);

    void addSubTaskToEpic(Epic epic, SubTask subTask);

    void updateTask(Task newTask);

    void updateSubTask(SubTask newSubTask, Epic epic);

    void updateEpic(Epic epic);

    void clearAllTasks();

    void clearAllSubTasks();

    void clearAllEpics();

    Optional<Task> getTaskById(int id);

    Optional<SubTask> getSubTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Set<SubTask> getEpicTasksListById(int id);

    void deleteTaskById(int id);

    void deleteSubTaskById(int id);

    void deleteEpicById(int id);

    void generateId(Task task);

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubTasks();

    Set<Task> getPrioritizedTasks();

    Set<SubTask> getPrioritizedEpicSubTasks(Epic epic);
}
