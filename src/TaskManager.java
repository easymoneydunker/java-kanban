import java.util.HashMap;

public class TaskManager {
    private int taskIdNumber = 1234567;
    private int subTaskIdNumber = 1;
    private int epicIdNumber = 7890276;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public void addTask(Task task) {
        if (task.getId() == 0) {
            generateId(task);
        }
        tasks.put(task.getId(), task);
    }

    public void addTask(Epic epic) {
        if (epic.getId() == 0) {
            generateId(epic);
        }
        epics.put(epic.getId(), epic);
    }
    public void addTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
}

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }

    public void updateEpicSubTaskBySubTaskId(Epic epic, int id, SubTask newSubTask) {
        if (id == newSubTask.getId())
            epic.updateSubTaskById(id, newSubTask);
    }

    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.remove(newTask.getId());
            tasks.put(newTask.getId(), newTask);
        } else {
            tasks.put(newTask.getId(), newTask);
        }
    }

    public void updateSubTask(SubTask newSubTask, Epic epic) {
        epic.updateSubTaskById(newSubTask.getId(), newSubTask);
        subTasks.put(newSubTask.getId(), newSubTask);
    }


    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllSubTasks() {
        subTasks.clear();
    }

    public void clearAllEpics() {
        epics.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
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

    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
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
