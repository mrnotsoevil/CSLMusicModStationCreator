package cslmusicmod.stationeditor.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cslmusicmod.stationeditor.model.adapters.ContextConditionAdapter;
import cslmusicmod.stationeditor.model.adapters.ContextConditionDNFAdapter;
import cslmusicmod.stationeditor.model.adapters.IntRangeAdapter;
import cslmusicmod.stationeditor.model.adapters.StationAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Station implements Validatable {

    private String name;
    private String thumbnail;
    private List<String> collections;
    private List<ScheduleEntry> schedule;
    private Map<String, ContextCondition> filters;
    private List<ContextEntry> contexts;

    private String directory;

    public Station() {
        name = "";
        thumbnail = "";
        collections = new ArrayList<>();
        schedule = new ArrayList<>();
        contexts = new ArrayList<>();
        filters = new HashMap<>();
    }

    public static Gson getGson() {
        GsonBuilder bson = new GsonBuilder();
        bson.registerTypeAdapter(IntRange.class, new IntRangeAdapter());
        bson.registerTypeAdapter(ContextCondition.class, new ContextConditionAdapter());
        bson.registerTypeAdapter(Station.class, new StationAdapter());
        bson.registerTypeAdapter(ContextConditionDNF.class, new ContextConditionDNFAdapter());
        bson.setPrettyPrinting();
        return bson.create();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public List<ScheduleEntry> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleEntry> schedule) {
        this.schedule = schedule;
    }

    public Map<String, ContextCondition> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, ContextCondition> filters) {
        this.filters = filters;
    }

    public List<ContextEntry> getContexts() {
        return contexts;
    }

    public void setContexts(List<ContextEntry> contexts) {
        this.contexts = contexts;
    }

    @Override
    public ValidationResult isValid() {

        return new ValidationResult(this)
                .and(!name.isEmpty(), "Name is empty")
                .and(schedule)
                .and(filters.values())
                .and(contexts);
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
