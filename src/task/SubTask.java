package task;

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


}
