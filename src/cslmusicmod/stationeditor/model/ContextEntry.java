package cslmusicmod.stationeditor.model;

import java.util.Collections;
import java.util.List;

public class ContextEntry implements Validatable {
    private ContextConditionDNF conditions;
    private List<String> collections;
    private List<String> songs;

    public ContextEntry() {
        conditions = new ContextConditionDNF();
        collections = Collections.emptyList();
        songs = Collections.emptyList();
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
}
