import java.util.HashMap;

public class TaskManager {
    public static int taskIdNumber = 1234567;
    public static int subTaskIdNumber = 1;
    public static int epicIdNumber = 7890276;
    public static HashMap<Integer, Task> tasks = new HashMap<>();

    public static void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public static void addSubTaskToEpic(Epic epic, SubTask subTask) {
        epic.addSubTask(subTask);
    }

    public static void updateEpicSubTaskBySubTaskId(Epic epic, int id, SubTask newSubTask) {
        if (id == newSubTask.getId())
            epic.updateSubTaskById(id, newSubTask);
    }

    public static void updateTaskById(int id, Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.remove(newTask.getId());
            tasks.put(id, newTask);
        } else {
            tasks.put(id, newTask);
        }
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

    public static HashMap<Integer, SubTask> getEpicTasksListById(int id) {
        Task epic = getTaskById(id);
        if (epic instanceof Epic) {
            return ((Epic) epic).getSubTasks();
        }
        return null;
    }

    public static void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public static int generateId(Task task) {
        if (task instanceof Epic) {
            return epicIdNumber++;
        } else if (task instanceof SubTask) {
            return subTaskIdNumber++;
        } else return taskIdNumber++;
    }
}
