import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskIdNumber = 1234567;
    private int subTaskIdNumber = 1;
    private int epicIdNumber = 7890276;
    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Override
    public void addTask(Task task) {
        if (task.getId() == 0) {
            generateId(task);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addTask(Epic epic) {
        if (epic.getId() == 0) {
            generateId(epic);
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void updateEpicSubTask(Epic epic, SubTask newSubTask) {
        epic.updateSubTask(newSubTask);
    }

    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.remove(newTask.getId());
            tasks.put(newTask.getId(), newTask);
        } else {
            tasks.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask, Epic epic) {
        epic.updateSubTask(newSubTask);
        subTasks.put(newSubTask.getId(), newSubTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.remove(epic.getId());
            epics.put(epic.getId(), epic);
        } else {
            epics.put(epic.getId(), epic);
        }
    }


    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            for (SubTask subTask : epic.getSubTasks().values()) {
                epic.removeSubTask(subTask);
            }
            epic.isDone();
        }
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        inMemoryHistoryManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public HashMap<Integer, SubTask> getEpicTasksListById(int id) {
        Task epic = getTaskById(id);
        if (epic instanceof Epic) {
            return ((Epic) epic).getSubTasks();
        }
        return null;
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        subTasks.remove(id);
        epic.removeSubTask(subTask);
        epic.isDone();

    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (SubTask subTask : epic.getSubTasks().values()) {
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    @Override
    public void generateId(Task task) {
        if (task instanceof Epic) {
            task.setId(epicIdNumber++);
        } else if (task instanceof SubTask) {
            task.setId(subTaskIdNumber++);
        } else {
            task.setId(taskIdNumber++);
        }
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

}
