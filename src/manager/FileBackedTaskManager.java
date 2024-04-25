package manager;

import task.*;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String filePath;

    public FileBackedTaskManager(String filePath) {
        this.filePath = filePath;
    }

    public FileBackedTaskManager(Path filePath) {
        this.filePath = filePath.toString();
    }

    public FileBackedTaskManager() {
        this.filePath = "src/data.csv";
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // skips "id,type,name,status,description,epic" line
            while (reader.ready()) {
                Task task = manager.fromString(reader.readLine());
                if (task instanceof Epic epic) {
                    manager.addTask(epic);
                } else if (task instanceof SubTask subTask) {
                    manager.addTask(subTask);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addTask(Epic epic) {
        super.addTask(epic);
        save();
    }

    @Override
    public void addTask(SubTask subTask) {
        super.addTask(subTask);
        save();
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("id,type,name,status,description,epic\n");
            for (Map.Entry<Integer, Task> taskEntry : super.getTasks().entrySet()) {
                bw.write(taskEntry.getValue().getId() + "," + TaskType.TASK + "," + taskEntry.getValue().getName() + "," + taskEntry.getValue().getStatus() + "," + taskEntry.getValue().getDescription() + "\n");
            }
            for (Map.Entry<Integer, Epic> taskEntry : super.getEpics().entrySet()) {
                bw.write(taskEntry.getValue().getId() + "," + TaskType.EPIC + "," + taskEntry.getValue().getName() + "," + taskEntry.getValue().getStatus() + "," + taskEntry.getValue().getDescription() + "\n");
            }
            for (Map.Entry<Integer, SubTask> taskEntry : super.getSubTasks().entrySet()) {
                bw.write(taskEntry.getValue().getId() + "," + TaskType.SUB_TASK + "," + taskEntry.getValue().getName() + "," + taskEntry.getValue().getStatus() + "," + taskEntry.getValue().getDescription() + "," + taskEntry.getValue().getEpicId() + "\n");
            }
        } catch (IOException e) {
            e.getMessage();
        }

    }

    private String taskToString(Task task) {
        return task.getId() + "," + TaskType.TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() +
                "\n";
    }

    private String taskToString(Epic task) {
        return task.getId() + "," + TaskType.EPIC + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() +
                "\n";
    }

    private String taskToString(SubTask task) {
        return task.getId() + "," + TaskType.SUB_TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + task.getEpicId() +
                "\n";
    }

    private Task fromString(String string) {
        String[] fields = string.split(",");
        TaskType type = null;
        Status status = null;
        Epic epic = null;
        if (fields[1].equalsIgnoreCase("task")) {
            type = TaskType.TASK;
        } else if (fields[1].equalsIgnoreCase("epic")) {
            type = TaskType.EPIC;
        } else if (fields[1].equalsIgnoreCase("sub_task")) {
            type = TaskType.SUB_TASK;
            epic = getEpics().get(Integer.parseInt(fields[5]));
        }
        if (fields[3].equalsIgnoreCase("new")) {
            status = Status.NEW;
        } else if (fields[3].equalsIgnoreCase("done")) {
            status = Status.DONE;
        } else if (fields[3].equalsIgnoreCase("in_progress")) {
            status = Status.IN_PROGRESS;
        }
        switch (type) {
            case TASK -> {
                return new Task(fields[2], Integer.parseInt(fields[0]), fields[4], status);
            }
            case EPIC -> {
                return new Epic(fields[2], Integer.parseInt(fields[0]), fields[4], status);
            }
            case SUB_TASK -> {
                return new SubTask(fields[2], Integer.parseInt(fields[0]), fields[4], status, epic);
            }
        }
        return null;
    }
}
