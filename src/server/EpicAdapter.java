package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.Epic;
import task.Status;
import task.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class EpicAdapter extends TypeAdapter<Epic> implements Adapter {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("status").value(epic.getStatus().toString());
        if (Objects.nonNull(epic.getDuration())) {
            jsonWriter.name("duration").value(epic.getDuration().getSeconds());
        }
        if (Objects.nonNull(epic.getStartTime())) {
            jsonWriter.name("startTime").value(epic.getStartTime().format(dateTimeFormatter));
        }
        jsonWriter.name("subTasks").beginArray();
        for (SubTask subTask : epic.getAllSubTasks()) {
            gson.toJson(subTask, SubTask.class, jsonWriter);
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
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

        Epic epic;
        if (Objects.nonNull(duration) && Objects.nonNull(startTime)) {
            epic = new Epic(name, id, description, status, startTime, duration);
        } else {
            epic = new Epic(name, id, description, status);
        }

        JsonArray subTasksArray = jsonObject.getAsJsonArray("subTasks");
        if (subTasksArray != null) {
            for (JsonElement subTaskElement : subTasksArray) {
                SubTask subTask = gson.fromJson(subTaskElement, SubTask.class);
                epic.addSubTask(subTask);
            }
        }
        return epic;
    }
}
