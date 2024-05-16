package historymanager;

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

    private void linkLast(Task task) {
        final TaskNode oldTail = tail;
        final TaskNode newNode = new TaskNode(oldTail, null, task);
        if (head == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        tail = newNode;
        history.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        TaskNode currentNode = head;
        while (currentNode != null) {
            list.add(currentNode.getTask());
            currentNode = currentNode.getNext();
        }
        return list;
    }

    private void removeNode(TaskNode taskNode) {
        if (taskNode == null) return;
        TaskNode prev = taskNode.getPrevious();
        TaskNode next = taskNode.getNext();

        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }

        if (next != null) {
            next.setPrevious(prev);
        } else {
            tail = prev;
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
        removeNode(taskNode);
    }

    @Override
    public Task getLastTask() {
        if (tail != null) {
            return tail.getTask();
        }
        return null;
    }

    public boolean containsId(int id) {
        return history.containsKey(id);
    }
}
