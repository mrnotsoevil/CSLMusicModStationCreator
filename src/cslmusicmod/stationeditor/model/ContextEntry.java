package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContextEntry implements Validatable {

    private transient Station station;

    private Formula conditions;
    private List<String> collections;
    private List<String> songs;
    private String name;

    public ContextEntry() {
        conditions = new Formula();
        collections = Collections.emptyList();
        songs = Collections.emptyList();
        name = "";
    }

    public ContextEntry(Station station) {
        this();
        this.station = station;
    }

    public ContextEntry(ContextEntry original, Station parent) {
        this.station = parent;
        this.conditions = new Formula(original.conditions, this);
        this.collections = new ArrayList<>(original.collections);
        this.songs = new ArrayList<>(original.songs);
        this.name = original.name;
    }

    public Formula getConditions() {
        return conditions;
    }

    public void setConditions(Formula conditions) {
        this.conditions = conditions;
    }

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and(conditions);
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getSummary() {
        return "Play " + (songs.isEmpty() ? "songs from " : songs.size() + " specific songs from ")
                + (collections.isEmpty() ? "<No collection>" : collections.stream().collect(Collectors.joining(", ")))
                + (conditions.isEmpty() ? "" : " if " + conditions.getSummary());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
