package server;

import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.Status;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TaskAdapter extends TypeAdapter<Task> implements Adapter {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(task.getName());
        jsonWriter.name("description").value(task.getDescription());
        jsonWriter.name("id").value(task.getId());
        jsonWriter.name("status").value(task.getStatus().toString());
        if (Objects.nonNull(task.getDuration())) {
            jsonWriter.name("duration").value(task.getDuration().getSeconds());
        }
        if (Objects.nonNull(task.getStartTime())) {
            jsonWriter.name("startTime").value(task.getStartTime().format(dateTimeFormatter));
        }
        jsonWriter.endObject();
    }

    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        int id = jsonObject.get("id").getAsInt();
        Status status = Status.valueOf(jsonObject.get("status").getAsString());

        Duration duration = null;
        LocalDateTime startTime = null;
        if (jsonObject.has("duration")) {
            duration = Duration.ofSeconds(jsonObject.get("duration").getAsLong());
        }
        if (jsonObject.has("startTime")) {
            startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), dateTimeFormatter);
        }

        if (Objects.nonNull(duration) && Objects.nonNull(startTime)) {
            return new Task(name, id, description, status, startTime, duration);
        }
        return new Task(name, id, description, status);
    }
}
