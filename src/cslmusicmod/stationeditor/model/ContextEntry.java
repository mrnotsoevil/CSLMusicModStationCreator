package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContextEntry implements Validatable {

    private transient Station station;

    private ContextConditionDNF conditions;
    private List<String> collections;
    private List<String> songs;

    public ContextEntry() {
        conditions = new ContextConditionDNF();
        collections = Collections.emptyList();
        songs = Collections.emptyList();
    }

    public ContextEntry(Station station) {
        this();
        this.station = station;
    }

    public ContextEntry(ContextEntry original) {
        this.station = original.station;
        this.conditions = new ContextConditionDNF(original.conditions);
        this.collections = new ArrayList<>(original.collections);
        this.songs = new ArrayList<>(original.songs);
    }

    public ContextConditionDNF getConditions() {
        return conditions;
    }

    public void setConditions(ContextConditionDNF conditions) {
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
}
