package cslmusicmod.stationeditor.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cslmusicmod.stationeditor.model.adapters.*;

import java.util.*;
import java.util.stream.Collectors;

public class Station implements Validatable {

    private String name;
    private String thumbnail;
    private List<String> collections;
    private List<ScheduleEntry> schedule;
    private Map<String, ContextCondition> filters;
    private List<ContextEntry> contexts;

    private transient String directory;

    public Station() {
        name = "";
        thumbnail = "";
        collections = new ArrayList<>();
        schedule = new ArrayList<>();
        contexts = new ArrayList<>();
        filters = new HashMap<>();
    }

    public Station(Station original) {
        this.name = original.name;
        this.thumbnail = original.thumbnail;
        this.collections = new ArrayList<>(original.collections);
        this.schedule = original.schedule.stream().map(x -> new ScheduleEntry(x)).collect(Collectors.toList());
        this.contexts = original.contexts.stream().map(x -> new ContextEntry(x)).collect(Collectors.toList());
        this.filters = new HashMap<>();
        original.filters.keySet().stream().forEach(x -> filters.put(x, original.filters.get(x)));
    }

    public static Gson getGson() {
        GsonBuilder bson = new GsonBuilder();
        bson.registerTypeAdapter(IntRange.class, new IntRangeAdapter());
        bson.registerTypeAdapter(ContextCondition.class, new ContextConditionAdapter());
        bson.registerTypeAdapter(Station.class, new StationAdapter());
        bson.registerTypeAdapter(Formula.class, new FormulaAdapter());
        bson.registerTypeAdapter(Conjunction.class, new ConjunctionAdapter());
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

    public String getFilterName(ContextCondition cond) {
        return getFilters().keySet().stream().filter(x -> getFilters().get(x) == cond).findAny().get();
    }

    public boolean renameFilter(String oldname, String newname) {
        if(newname.isEmpty())
            return false;
        if(oldname.equals(newname))
            return true;
        if(getFilters().containsKey(newname))
            return false;
        if(!getFilters().containsKey(oldname))
            return false;

        ContextCondition cond = filters.get(oldname);
        filters.remove(oldname);
        filters.put(newname, cond);

        // Rename in DNF
        contexts.stream().forEach(context -> {
            context.getConditions().getDnf().stream().forEach(conj -> {
                List<String> list = conj.getLiterals();
                for(int i = 0; i < list.size(); ++i) {
                    if(list.get(i).equals(oldname)) {
                        list.set(i, newname);
                    }
                }
            });
        });

        return true;
    }
}
