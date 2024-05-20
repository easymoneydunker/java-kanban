package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private Epic epic;
    private int epicId;

    public SubTask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        this.epicId = epic.getId();
        this.epic.updateSubTask(this);
    }

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, int id, String description, Status status, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, id, description, status, startTime, duration);
        this.epic = epic;
        this.epicId = epic.getId();
    }

    public SubTask(String name, int id, String description, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, id, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epic = epic;
        this.epicId = epic.getId();
    }

    public SubTask(String name, int id, String description, Status status, Epic epic) {
        super(name, id, description, status);
        this.epic = epic;
        this.epicId = epic.getId();
    }

    public SubTask(String name, int id, String description, Status status, int epicId) {
        super(name, id, description, status);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epic.getId();
    }

    @Override
    public String toString() {
        return "task.Task{" + "name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id=" +
                getId() + ", status=" + getStatus() + ", epicId=" + epicId + "}";
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
        return Objects.hash(super.getName(), super.getDescription(), super.getId(), super.getStatus(), epic);
    }

    public Epic getEpic() {
        return epic;
    }
}
