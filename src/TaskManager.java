import java.util.ArrayList;

public class TaskManager {
    public static int idNumber = 1234567;
    public static ArrayList<Task> tasks = new ArrayList<>();

    public static int generateId() {
        return idNumber++;
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void updateTaskById(int id, Task newTask) {
        Task taskToUpdate = getTaskById(id);
        if (taskToUpdate != null) {
            taskToUpdate = newTask;
        }
    }

    public static void printTasks() {
        tasks.forEach(System.out::println);
    }

    public static void clearAllTasks() {
        tasks.clear();
    }

    public static Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public static ArrayList<SubTask> getEpicTasksListById(int id) {
        Task epic = getTaskById(id);
        if (epic instanceof Epic) {
            return ((Epic) epic).getSubTasks();
        }
        return null;
    }

    public static void deleteTaskById(int id) {
        tasks.remove(getTaskById(id));
    }
}
