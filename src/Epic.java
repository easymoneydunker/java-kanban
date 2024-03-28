import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);

    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        isDone();
    }


    public void updateSubTaskById(int id, SubTask newSubTask) {
        subTasks.remove(newSubTask.getId());
        subTasks.put(id, newSubTask);
        isDone();
    }

    public void isDone() {
        boolean isDone;
        for (Integer id : subTasks.keySet()) {
            if (subTasks.get(id).getStatus() != Status.DONE) {
                return;
            }
        }
        setStatus(Status.DONE);
    }

    @Override
    public String toString() {
        StringBuilder epic = new StringBuilder("Epic{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subTasks=");
        for (Integer id : subTasks.keySet()) {
            epic.append(subTasks.get(id).toString());
        }
        epic.append("}");
        return epic.toString();
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
}

