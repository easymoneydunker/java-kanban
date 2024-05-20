package task;

import manager.ManagerRuntimeException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final TreeSet<SubTask> sortedSubTasks;
    private final HashSet<SubTask> allSubTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        sortedSubTasks = new TreeSet<>((sub1, sub2) -> {
            LocalDateTime endTime1 = sub1.getEndTime();
            LocalDateTime endTime2 = sub2.getEndTime();

            if (endTime1 == null && endTime2 == null) {
                return 0;
            } else if (endTime1 == null) {
                return -1;
            } else if (endTime2 == null) {
                return 1;
            } else {
                return endTime1.compareTo(endTime2);
            }
        });
        allSubTasks = new HashSet<>();
    }

    public Epic(String name, int id, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, id, description, status, startTime, duration);
        sortedSubTasks = new TreeSet<>((sub1, sub2) -> sub1.getEndTime().compareTo(sub2.getEndTime()));
        allSubTasks = new HashSet<>();
    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        sortedSubTasks = new TreeSet<>((sub1, sub2) -> sub1.getEndTime().compareTo(sub2.getEndTime()));
        allSubTasks = new HashSet<>();
    }

    public Epic(String name, int id, String description, Status status) {
        super(name, id, description, status);
        sortedSubTasks = new TreeSet<>((sub1, sub2) -> sub1.getEndTime().compareTo(sub2.getEndTime()));
        allSubTasks = new HashSet<>();
    }

    public void addSubTask(SubTask subTask) {
        if (subTask.getStartTime() != null) {
            Set<SubTask> prioritizedSubTasks = getSortedSubTasks();
            Optional<SubTask> overlappingSubTask = prioritizedSubTasks.stream().filter(task1 -> task1.getStartTime() != null)
                    .filter(task1 -> (task1.getStartTime().isBefore(subTask.getEndTime()) && task1.getEndTime().isAfter(subTask.getStartTime())) ||
                            (task1.getStartTime().isAfter(subTask.getStartTime()) && task1.getEndTime().isBefore(subTask.getEndTime())) ||
                            (task1.getStartTime().isEqual(subTask.getStartTime()) || task1.getEndTime().isEqual(subTask.getEndTime())))
                    .findAny();
            if (overlappingSubTask.isPresent()) {
                throw new ManagerRuntimeException("Task overlapping exception");
            }
            sortedSubTasks.add(subTask);
        }
        allSubTasks.add(subTask);
        isDone();
    }

    public void removeSubTask(SubTask subTask) {
        sortedSubTasks.remove(subTask);
        allSubTasks.remove(subTask);
    }


    public void updateSubTask(SubTask newSubTask) {
        sortedSubTasks.remove(newSubTask);
        sortedSubTasks.add(newSubTask);
        isDone();
    }

    public void isDone() {
        boolean anyInProgress = sortedSubTasks.stream()
                .anyMatch(subTask -> subTask.getStatus() == Status.IN_PROGRESS);

        boolean allNew = sortedSubTasks.stream()
                .allMatch(subTask -> subTask.getStatus() == Status.NEW);

        boolean anyNew = sortedSubTasks.stream()
                .anyMatch(subTask -> subTask.getStatus() == Status.NEW);

        if (anyInProgress || (anyNew && !allNew)) {
            setStatus(Status.IN_PROGRESS);
        } else if (allNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.DONE);
        }
    }


    @Override
    public String toString() {
        StringBuilder epic = new StringBuilder("task.Epic{" + "name=" + getName() + ", description=" + getDescription() + ", id=" + getId() + ", status=" + getStatus() + ", sortedSubTasks=");
        for (SubTask subTask : sortedSubTasks) {
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
        return getSortedSubTasks().equals(epic.getSortedSubTasks()) && getAllSubTasks().equals(epic.getAllSubTasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), super.getDescription(), super.getId(), super.getStatus());
    }


    public Set<SubTask> getSortedSubTasks() {
        return sortedSubTasks;
    }

    @Override
    public LocalDateTime getEndTime() {
        int duration = sortedSubTasks.stream().mapToInt(subTask -> Math.toIntExact(subTask.getDuration().toSeconds())).sum();
        return getStartTime().plusSeconds(duration);
    }

    public HashSet<SubTask> getAllSubTasks() {
        return allSubTasks;
    }

    public void removeAllSubTasks() {
        sortedSubTasks.clear();
        allSubTasks.clear();
    }
}

