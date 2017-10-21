package cslmusicmod.stationeditor.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cslmusicmod.stationeditor.model.adapters.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Station implements Validatable {

    private String name;
    private String description;
    private String thumbnail;
    private Map<String, SongCollection> collections;
    private List<ScheduleEntry> schedule;
    private Map<String, ContextCondition> filters;
    private List<ContextEntry> contexts;

    private transient String filename;
    private transient String directory;

    public Station() {
        name = "";
        description = "Adds more music to the game. Needs CSL Music Mod.";
        thumbnail = "";
        collections = new HashMap<>();
        schedule = new ArrayList<>();
        contexts = new ArrayList<>();
        filters = new HashMap<>();
    }

    public Station(Station original) {
        this.name = original.name;
        this.description = original.description;
        this.thumbnail = original.thumbnail;
        this.collections = new HashMap<>(original.collections);
        this.schedule = original.schedule.stream().map(x -> new ScheduleEntry(x, this)).collect(Collectors.toList());
        this.contexts = original.contexts.stream().map(x -> new ContextEntry(x, this)).collect(Collectors.toList());
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
        bson.registerTypeAdapter(SongCollection.class, new SongCollectionAdapter());
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

    public Map<String, SongCollection> getCollections() {
        return Collections.unmodifiableMap(collections);
    }

    public void addCollection(SongCollection coll) {
        if(!collections.values().contains(coll) && !collections.keySet().contains(coll.getName())) {
            collections.put(coll.getName(), coll);
        }
    }

    public boolean canRemoveCollection(SongCollection coll) {
        return contexts.stream().allMatch(context -> {
            return !context.getCollections().contains(coll.getName());
        });
    }

    public void removeCollection(SongCollection coll) {
        if(canRemoveCollection(coll))
            collections.remove(coll.getName());
    }

    public List<ScheduleEntry> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleEntry> schedule) {
        this.schedule = schedule;
    }

    public Map<String, ContextCondition> getFilters() {
        return Collections.unmodifiableMap(filters);
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

    public void addFilter(String name, ContextCondition cond) {
        this.filters.put(name, cond);
    }

    public boolean canRemoveFilter(String name) {
        return contexts.stream().allMatch(context -> {
           return context.getConditions().getConjunctions().stream().allMatch(cond -> !cond.getLiterals().contains(name));
        });
    }

    public boolean removeFilter(String name) {
        if(!canRemoveFilter(name))
            return false;

        filters.remove(name);

        return true;
    }

    public boolean renameFilter(String oldname, String newname) {
        if (newname.isEmpty())
            return false;
        if (oldname.equals(newname))
            return true;
        if (getFilters().containsKey(newname))
            return false;
        if (!getFilters().containsKey(oldname))
            return false;

        ContextCondition cond = filters.get(oldname);
        filters.remove(oldname);
        filters.put(newname, cond);

        // Rename in DNF
        contexts.stream().forEach(context -> {
            context.getConditions().getConjunctions().stream().forEach(conj -> {
                List<String> list = conj.getLiterals();
                for (int i = 0; i < list.size(); ++i) {
                    if (list.get(i).equals(oldname)) {
                        list.set(i, newname);
                    }
                }
            });
        });

        return true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String buildModSource() {
        String template;
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/cslmusicmod/stationeditor/Mod.cs")))) {
            template = buffer.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        String namespace = "MusicPack_" + getName().trim().replaceAll("[^a-zA-Z0-9]" , "_");

        template = template.replace("__NAME__", getName())
                .replace("__DESCRIPTION__", getDescription())
                .replace("__NAMESPACE__", namespace);

        return template;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean hasSaveLocation() {
        return filename != null && Files.exists(Paths.get(filename));
    }

    public List<File> getExportableFiles() throws IOException {

        if(!hasSaveLocation())
            return Collections.emptyList();

        List<File> export = new ArrayList<>();

        export.add(new File(filename));
        if(thumbnail != null && !thumbnail.isEmpty())
            export.add(Paths.get(directory).resolve(thumbnail).toFile());

        for(SongCollection coll : collections.values()) {
            if(coll.isEditable()) {
               Path parent = Paths.get(directory).resolve(coll.getName());

               if(Files.exists(parent)) {
                   Files.list(parent).forEach(file -> {
                       String extension = com.google.common.io.Files.getFileExtension(file.toString()).toLowerCase();

                       if(extension.equals("ogg")) {
                           export.add(file.toFile());
                       }
                   });
               }
            }
        }

        return export;
    }
}
