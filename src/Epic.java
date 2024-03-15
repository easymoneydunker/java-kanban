import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> subTasks;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }
}
