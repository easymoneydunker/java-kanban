import java.util.HashMap;

public interface TaskManager {
    void addTask(Task task);

    void addTask(Epic epic);

    void addTask(SubTask subTask);

    void addSubTaskToEpic(Epic epic, SubTask subTask);

    void updateEpicSubTask(Epic epic, SubTask newSubTask);

    void updateTask(Task newTask);

    void updateSubTask(SubTask newSubTask, Epic epic);

    void updateEpic(Epic epic);

    void clearAllTasks();

    void clearAllSubTasks();

    void clearAllEpics();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    HashMap<Integer, SubTask> getEpicTasksListById(int id);

    void deleteTaskById(int id);

    void deleteSubTaskById(int id);

    void deleteEpicById(int id);

    void generateId(Task task);

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubTasks();
}
