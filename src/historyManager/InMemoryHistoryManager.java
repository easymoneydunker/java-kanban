package historyManager;

import node.TaskNode;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, TaskNode> history;
    private TaskNode head = null;
    private TaskNode tail = null;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {
        final TaskNode oldTail = tail;
        final TaskNode newNode = new TaskNode(oldTail, null, task);
        if (oldTail == null) {
            head = newNode;
            tail = newNode;
            head.setNext(tail);
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
        }
        tail = newNode;
        history.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        TaskNode currentNode = head;
        list.add(currentNode.getTask());
        while (currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
            list.add(currentNode.getTask());
        }
        return list;
    }

    public void removeNode(TaskNode taskNode) {
        TaskNode newNext = null;
        TaskNode newPrev = null;
        if (taskNode.getNext() != null) {
            newNext = history.get(taskNode.getNext().getTask().getId());
        }
        if (taskNode.getPrevious() != null) {
            newPrev = history.get(taskNode.getPrevious().getTask().getId());
        }
        if (taskNode.equals(head)) {
            head.getNext().setPrevious(null);
            head = head.getNext();
        } else if (taskNode.equals(tail)) {
            tail.getPrevious().setNext(null);
            tail = tail.getPrevious();
        }
        if (history.get(taskNode.getTask().getId()).getPrevious() != null) {
            history.get(taskNode.getTask().getId()).getPrevious().setNext(newNext);
        }
        if (history.get(taskNode.getTask().getId()).getNext() != null) {
            history.get(taskNode.getTask().getId()).getNext().setPrevious(newPrev);
        }
        history.remove(taskNode.getTask().getId());
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            TaskNode taskNode = history.get(task.getId());
            removeNode(taskNode);
        }
        linkLast(task);
    }

    @Override
    public void remove(Task task) {
        TaskNode taskNode = history.get(task.getId());
        if (taskNode != null) {
            removeNode(taskNode);
        }

        history.remove(task.getId());
    }

    public Task getLastTask() {
        return tail.getTask();
    }

    public boolean containsId(int id) {
        return history.containsKey(id);
    }
}
