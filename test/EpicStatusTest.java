import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {

    @Test
    void testAllSubTasksNew() {
        // Arrange
        Epic epic = new Epic("Epic 1", "Description", Status.NEW);
        Set<SubTask> subTasks = new HashSet<>(Arrays.asList(
                new SubTask("SubTask 1", 12345678, "Description", Status.NEW, epic, LocalDateTime.now(), Duration.ofHours(1)),
                new SubTask("SubTask 2", 12345679, "Description", Status.NEW, epic, LocalDateTime.now().plusDays(1), Duration.ofHours(2))));
        subTasks.forEach(epic::addSubTask);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testAllSubTasksDone() {
        // Arrange
        Epic epic = new Epic("Epic 2", "Description", Status.NEW);
        Set<SubTask> subTasks = new HashSet<>(Arrays.asList(new SubTask("SubTask 1", 12345678, "Description", Status.DONE, epic, LocalDateTime.now(), Duration.ofHours(1)),
                new SubTask("SubTask 2", 12345679, "Description", Status.DONE, epic, LocalDateTime.now().plusDays(1), Duration.ofHours(2))));
        subTasks.forEach(epic::addSubTask);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void testSubTasksNewAndDone() {
        Epic epic = new Epic("Epic 3", "Description", Status.NEW);
        Set<SubTask> subTasks = new HashSet<>(Arrays.asList(new SubTask("SubTask 1", 12345678, "Description", Status.NEW, epic, LocalDateTime.now(), Duration.ofHours(1)),
                new SubTask("SubTask 2", 12345679, "Description", Status.DONE, epic, LocalDateTime.now().plusDays(1), Duration.ofHours(2))));
        subTasks.forEach(epic::addSubTask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testSubTasksInProgress() {
        Epic epic = new Epic("Epic 4", "Description", Status.NEW);
        Set<SubTask> subTasks = new HashSet<>(Arrays.asList(new SubTask("SubTask 1", 1234567, "Description", Status.IN_PROGRESS, epic, LocalDateTime.now(), Duration.ofHours(1)),
                new SubTask("SubTask 2", 1234568, "Description", Status.IN_PROGRESS, epic, LocalDateTime.now().plusDays(1), Duration.ofHours(2))));
        subTasks.forEach(epic::addSubTask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}
