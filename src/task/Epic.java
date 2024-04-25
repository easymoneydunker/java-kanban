package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public Epic(String name, int id, String description, Status status) {
        super(name, id, description, status);
        subTasks = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        isDone();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
    }


    public void updateSubTask(SubTask newSubTask) {
        subTasks.remove(newSubTask);
        subTasks.add(newSubTask);
        isDone();
    }

    public void isDone() {
        boolean isDone;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != Status.DONE) {
                setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        setStatus(Status.DONE);
    }

    @Override
    public String toString() {
        StringBuilder epic = new StringBuilder("task.Epic{" + "name=" + getName() + ", description=" + getDescription() + ", id=" + getId() + ", status=" + getStatus() + ", subTasks=");
        for (SubTask subTask : subTasks) {
            epic.append(subTask.toString());
        }
        epic.append("}");
        return epic.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getSubTasks().equals(epic.getSubTasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSubTasks());
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }
}

