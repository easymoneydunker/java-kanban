package manager;

import task.*;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String filePath;
    private final TreeSet<Task> taskTreeSet = new TreeSet<>((task1, task2) -> task1.getEndTime().compareTo(task2.getEndTime()));
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


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
        FileBackedTaskManager manager = new FileBackedTaskManager(file.toPath());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
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
            throw new RuntimeException(e);
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        taskTreeSet.add(task);
    }

    @Override
    public void addTask(Epic epic) {
        super.addTask(epic);
        taskTreeSet.add(epic);
    }

    @Override
    public void addTask(SubTask subTask) {
        super.addTask(subTask);
    }

    public void save(Path path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()))) {
            bw.write("id,type,name,status,description,epic\n");
            for (Map.Entry<Integer, Task> taskEntry : super.getTasks().entrySet()) {
                bw.write(taskToString(taskEntry.getValue()) + "\n");
            }
            for (Map.Entry<Integer, Epic> taskEntry : super.getEpics().entrySet()) {
                bw.write(taskToString(taskEntry.getValue()) + "\n");
            }
            for (Map.Entry<Integer, SubTask> taskEntry : super.getSubTasks().entrySet()) {
                bw.write(taskToString(taskEntry.getValue()) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        save(Path.of(filePath));
    }

    private Task fromString(String string) {
        String[] fields = string.split(","); //id,type,name,status,description,startTime,duration,epic
        TaskType type = null;
        Status status;
        Epic epic = null;
        LocalDateTime startTime;
        String description;
        Duration duration;
        String name;

        int id = Integer.parseInt(fields[0]);
        String statusField = fields[3].toUpperCase();
        name = fields[2];
        if (fields[1].equalsIgnoreCase("task") || fields[1].equalsIgnoreCase("task.task")) {
            type = TaskType.TASK;
        } else if (fields[1].equalsIgnoreCase("epic") || fields[1].equalsIgnoreCase("task.epic")) {
            type = TaskType.EPIC;
        } else if (fields[1].equalsIgnoreCase("subtask") || fields[1].equalsIgnoreCase("task.subtask")) {
            type = TaskType.SUB_TASK;
            if (fields.length < 7) {
                epic = getEpics().get(Integer.parseInt(fields[5]));
            } else {
                epic = getEpics().get(Integer.parseInt(fields[7]));
            }
        }
        switch (statusField) {
            case "NEW" -> status = Status.NEW;
            case "DONE" -> status = Status.DONE;
            case "IN_PROGRESS" -> status = Status.IN_PROGRESS;
            default -> throw new IllegalArgumentException("Unrecognized status: " + fields[3]);
        }

        description = fields[4];
        startTime = LocalDateTime.parse(fields[5], formatter);
        duration = Duration.ofMinutes(Integer.parseInt(fields[6]));
        switch (Objects.requireNonNull(type)) {
            case TASK -> {
                return new Task(name, id, description, status, startTime, duration);
            }
            case EPIC -> {
                return new Epic(name, id, description, status, startTime, duration);
            }
            case SUB_TASK -> {
                return new SubTask(name, id, description, status, epic, startTime, duration);
            }
        }
        return null;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return taskTreeSet;
    }

    private String taskToString(Task task) {
        if (task.getStartTime() != null) {
            return String.format("%s,%s,%s,%s,%s,%s,%d", task.getId(), task.getClass().getName().toLowerCase(), task.getName(), task.getStatus(), task.getDescription(), task.getStartTime().format(formatter), task.getDuration().toMinutes());
        } else {
            return String.format("%s,%s,%s,%s,%s", task.getId(), task.getClass().getName().toLowerCase(), task.getName(), task.getStatus(), task.getDescription());
        }
    }

    private String taskToString(SubTask subTask) {
        return taskToString((Task) subTask) + "," + subTask.getEpicId();
    }
}
