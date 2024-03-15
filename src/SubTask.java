public class SubTask extends Task {
    private final Epic epic;

    public SubTask(String name, String description, int id, Status status, Epic epic) {
        super(name, description, id, status);
        this.epic = epic;
        this.epic.addSubTask(this);
    }
}
