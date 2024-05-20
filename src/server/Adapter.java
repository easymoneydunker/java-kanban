package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.Epic;
import task.SubTask;
import task.Task;

interface Adapter {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .create();
}
