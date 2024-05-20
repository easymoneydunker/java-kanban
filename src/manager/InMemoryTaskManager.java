package manager;

import historymanager.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private final TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getEndTime));
    private int taskIdNumber = 1234567;
    private int subTaskIdNumber = 1;
    private int epicIdNumber = 7890276;

    @Override
    public void addTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            updateTask(task);
            return;
        }
        if (task.getId() == 0) {
            generateId(task);
        }
        if (task.getStartTime() != null) {
            Set<Task> prioritized = getPrioritizedTasks();
            Optional<Task> overlappingTask = prioritized.stream().filter(task1 -> task1.getStartTime() != null).filter(task1 -> (task1.getStartTime().isBefore(task.getEndTime()) && task1.getEndTime().isAfter(task.getStartTime())) || (task1.getStartTime().isAfter(task.getStartTime()) && task1.getEndTime().isBefore(task.getEndTime())) || (task1.getStartTime().isEqual(task.getStartTime()) || task1.getEndTime().isEqual(task.getEndTime()))).findAny();
            if (overlappingTask.isPresent()) {
                throw new TaskOverlappingException("Task overlapping exception");
            }
            sortedTasks.add(task);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addTask(Epic epic) {
        if (tasks.containsKey(epic.getId())) {
            updateEpic(epic);
            return;
        }
        if (epic.getId() == 0) {
            generateId(epic);
        }
        if (epic.getStartTime() != null) {
            Set<Task> prioritized = getPrioritizedTasks();
            Optional<Task> overlappingTask = prioritized.stream().filter(task1 -> task1.getStartTime() != null).filter(task1 -> (task1.getStartTime().isBefore(epic.getEndTime()) && task1.getEndTime().isAfter(epic.getStartTime())) || (task1.getStartTime().isAfter(epic.getStartTime()) && task1.getEndTime().isBefore(epic.getEndTime())) || (task1.getStartTime().isEqual(epic.getStartTime()) || task1.getEndTime().isEqual(epic.getEndTime()))).findAny();
            if (overlappingTask.isPresent()) {
                throw new TaskOverlappingException("Task overlapping exception");
            }
            sortedTasks.add(epic);
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addTask(SubTask subTask) {
        if (subTask.getId() == 0) {
            generateId(subTask);
        }
        if (subTask.getStartTime() != null) {
            Set<SubTask> prioritizedSubTasks = epics.get(subTask.getEpicId()).getSortedSubTasks();
            Optional<SubTask> overlappingSubTask = prioritizedSubTasks.stream().filter(task1 -> task1.getStartTime() != null).filter(task1 -> (task1.getStartTime().isBefore(subTask.getEndTime()) && task1.getEndTime().isAfter(subTask.getStartTime())) || (task1.getStartTime().isAfter(subTask.getStartTime()) && task1.getEndTime().isBefore(subTask.getEndTime())) || (task1.getStartTime().isEqual(subTask.getStartTime()) || task1.getEndTime().isEqual(subTask.getEndTime()))).findAny();
            if (overlappingSubTask.isPresent()) {
                throw new TaskOverlappingException("Task overlapping exception");
            }
        }
        epics.get(subTask.getEpicId()).addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }


    @Override
    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        if (subTask.getId() == 0) {
            generateId(subTask);
        }
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void updateTask(Task newTask) {
        sortedTasks.remove(tasks.get(newTask.getId()));
        tasks.remove(newTask.getId());
        if (newTask.getStartTime() != null) {
            Set<Task> prioritized = getPrioritizedTasks();
            Optional<Task> overlappingTask = prioritized.stream().filter(task1 -> task1.getStartTime() != null).filter(task1 -> (task1.getStartTime().isBefore(newTask.getEndTime()) && task1.getEndTime().isAfter(newTask.getStartTime())) || (task1.getStartTime().isAfter(newTask.getStartTime()) && task1.getEndTime().isBefore(newTask.getEndTime())) || (task1.getStartTime().isEqual(newTask.getStartTime()) || task1.getEndTime().isEqual(newTask.getEndTime()))).findAny();
            if (overlappingTask.isPresent()) {
                throw new TaskOverlappingException("Task overlapping exception");
            }
            sortedTasks.add(newTask);
        }
        tasks.put(newTask.getId(), newTask);
    }

    @Override
    public void updateSubTask(SubTask newSubTask, Epic epic) {
        epic.updateSubTask(newSubTask);
        subTasks.remove(newSubTask.getId());
        subTasks.put(newSubTask.getId(), newSubTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        sortedTasks.remove(tasks.get(epic.getId()));
        tasks.remove(epic.getId());
        if (epic.getStartTime() != null) {
            Set<Task> prioritized = getPrioritizedTasks();
            Optional<Task> overlappingTask = prioritized.stream().filter(task1 -> task1.getStartTime() != null).filter(task1 -> (task1.getStartTime().isBefore(epic.getEndTime()) && task1.getEndTime().isAfter(epic.getStartTime())) || (task1.getStartTime().isAfter(epic.getStartTime()) && task1.getEndTime().isBefore(epic.getEndTime())) || (task1.getStartTime().isEqual(epic.getStartTime()) || task1.getEndTime().isEqual(epic.getEndTime()))).findAny();
            if (overlappingTask.isPresent()) {
                throw new TaskOverlappingException("Task overlapping exception");
            }
            sortedTasks.add(epic);
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void clearAllTasks() {
        tasks.values().forEach(inMemoryHistoryManager::remove);
        tasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        subTasks.values().forEach(inMemoryHistoryManager::remove);
        subTasks.clear();
        for (Epic epic : epics.values()) {
            for (SubTask subTask : epic.getSortedSubTasks()) {
                epic.removeSubTask(subTask);
            }
            epic.isDone();
        }
        epics.values().stream().peek(Epic::removeAllSubTasks);
    }

    @Override
    public void clearAllEpics() {
        epics.values().forEach(inMemoryHistoryManager::remove);
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Optional<SubTask> getSubTaskById(int id) {
        inMemoryHistoryManager.add(subTasks.get(id));
        return Optional.ofNullable(subTasks.get(id));
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return Optional.ofNullable(epics.get(id));
    }

    @Override
    public Set<SubTask> getEpicTasksListById(int id) {
        Optional<Epic> optionalEpic = getEpicById(id);
        if (optionalEpic.isPresent()) {
            Epic epic = optionalEpic.get();
            return epic.getSortedSubTasks();
        }
        return null;
    }

    @Override
    public void deleteTaskById(int id) {
        inMemoryHistoryManager.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        inMemoryHistoryManager.remove(subTasks.get(id));
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        subTasks.remove(id);
        epic.removeSubTask(subTask);
        epic.isDone();
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (SubTask subTask : epic.getAllSubTasks()) {
            subTasks.remove(subTask.getId());
        }
        inMemoryHistoryManager.remove(epic);
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

    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public boolean containsHistoryId(int id) {
        return inMemoryHistoryManager.containsId(id);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    @Override
    public Set<SubTask> getPrioritizedEpicSubTasks(Epic epic) {
        return epic.getSortedSubTasks();
    }
}
