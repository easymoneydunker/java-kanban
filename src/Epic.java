import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public boolean isDone() {
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subTasks=" + subTasks +
                '}';
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }
}
