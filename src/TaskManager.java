import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int idNumber = 1234567;
    public static HashMap<Integer, Task> tasks = new HashMap<>();

    public static int generateId() {
        return idNumber++;
    }

    public static void addTask(Task task) {
        tasks.put(generateId(), task);
    }

    public static void updateTaskById(int id, Task newTask) {
        tasks.put(id, newTask);
    }

    public static void printTasks() {
        for (Integer id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
    }

    public static void clearAllTasks() {
        tasks.clear();
    }

    public static Task getTaskById(int id) {
        return tasks.get(id);
    }

    public static ArrayList<SubTask> getEpicTasksListById(int id) {
        Task epic = getTaskById(id);
        if (epic instanceof Epic) {
            return ((Epic) epic).getSubTasks();
        }
        return null;
    }

    public static void deleteTaskById(int id) {
        tasks.remove(id);
    }
}
