package task;

import java.util.Objects;

public class SubTask extends Task {
    private final Epic epic;

    public SubTask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        this.epic.updateSubTask(this);
    }

    public SubTask(String name, int id, String description, Status status, Epic epic) {
        super(name, id, description, status);
        this.epic = epic;
    }


    public int getEpicId() {
        return epic.getId();
    }

    @Override
    public String toString() {
        return "task.Task{" + "name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id=" + getId() + ", status=" + getStatus() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epic.equals(subTask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }
}
