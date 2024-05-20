package server;

import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.Status;
import task.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SubTaskAdapter extends TypeAdapter<SubTask> implements Adapter {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(subTask.getName());
        jsonWriter.name("description").value(subTask.getDescription());
        jsonWriter.name("id").value(subTask.getId());
        jsonWriter.name("status").value(subTask.getStatus().toString());
        if (Objects.nonNull(subTask.getDuration())) {
            jsonWriter.name("duration").value(subTask.getDuration().getSeconds());
        }
        if (Objects.nonNull(subTask.getStartTime())) {
            jsonWriter.name("startTime").value(subTask.getStartTime().format(dateTimeFormatter));
        }
        jsonWriter.name("epic").value(subTask.getEpicId());
        jsonWriter.endObject();
    }

    @Override
    public SubTask read(JsonReader jsonReader) throws IOException {
        JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        int id = jsonObject.get("id").getAsInt();
        Status status = Status.valueOf(jsonObject.get("status").getAsString());
        int epicId = jsonObject.get("epic").getAsInt();

        Duration duration = null;
        LocalDateTime startTime = null;
        if (jsonObject.has("duration")) {
            duration = Duration.ofSeconds(jsonObject.get("duration").getAsLong());
        }
        if (jsonObject.has("startTime")) {
            startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), dateTimeFormatter);
        }

        if (Objects.nonNull(duration) && Objects.nonNull(startTime)) {
            return new SubTask(name, id, description, status, epicId, startTime, duration);
        }
        return new SubTask(name, id, description, status, epicId);
    }
}

