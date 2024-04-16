package node;

import task.Task;

import java.util.Objects;

public class TaskNode {
    private TaskNode next;
    private TaskNode previous;
    private Task task;

    public TaskNode(TaskNode previous, TaskNode next, Task task) {
        this.previous = previous;
        this.next = next;
        this.task = task;
    }

    public TaskNode getNext() {
        return next;
    }

    public void setNext(TaskNode next) {
        this.next = next;
    }

    public TaskNode getPrevious() {
        return previous;
    }

    public void setPrevious(TaskNode previous) {
        this.previous = previous;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskNode taskNode = (TaskNode) o;
        return Objects.equals(next, taskNode.next) && Objects.equals(previous, taskNode.previous) && task.equals(taskNode.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task);
    }

    @Override
    public String toString() {
        return "TaskNode{" + ", task=" + task + '}';
    }
}
