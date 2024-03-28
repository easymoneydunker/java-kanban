import java.util.HashMap;

public class TaskManager {
    private int taskIdNumber = 1234567;
    private int subTaskIdNumber = 1;
    private int epicIdNumber = 7890276;
    private static final HashMap<Integer, Task> tasks = new HashMap<>();
    private static final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static final HashMap<Integer, Epic> epics = new HashMap<>();

    public void addTask(Task task) {
        if (task.getId() == 0) {
            generateId(task);
        }
        if (task instanceof Epic epic) {
            epics.put(task.getId(), epic);
        } else if (task instanceof SubTask subTask) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.addSubTask(subTask);
            subTasks.put(subTask.getId(), subTask);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }

    public void updateEpicSubTaskBySubTaskId(Epic epic, int id, SubTask newSubTask) {
        if (id == newSubTask.getId())
            epic.updateSubTaskById(id, newSubTask);
    }

    public void updateTaskById(int id, Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.remove(newTask.getId());
            tasks.put(id, newTask);
        } else {
            tasks.put(id, newTask);
        }
    }

    public void updateSubTaskById(int id, SubTask newSubTask, Epic epic) {
        epic.updateSubTaskById(id, newSubTask);
        subTasks.put(id, newSubTask);
    }


    public void clearAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public HashMap<Integer, SubTask> getEpicTasksListById(int id) {
        Task epic = getTaskById(id);
        if (epic instanceof Epic) {
            return ((Epic) epic).getSubTasks();
        }
        return null;
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void generateId(Task task) {
        if (task instanceof Epic) {
            task.setId(epicIdNumber++);
        } else if (task instanceof SubTask) {
            task.setId(subTaskIdNumber++);
        } else {
            task.setId(taskIdNumber++);
        }
    }
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

}
