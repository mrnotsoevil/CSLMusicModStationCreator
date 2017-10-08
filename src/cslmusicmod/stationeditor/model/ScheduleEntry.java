package cslmusicmod.stationeditor.model;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScheduleEntry implements Validatable, IntRanged {

    public static List<String> ALLOWED_TYPES = Collections.unmodifiableList(Arrays.asList("music",
            "blurb",
            "broadcast",
            "commercial",
            "talk"));

    private transient Station station;

    private String type;
    private int min;
    private int max;

    public ScheduleEntry() {
        type = "music";
        min = 0;
        max = 3;
    }

    public ScheduleEntry(ScheduleEntry original) {
        this.station = original.station;
        this.type = original.type;
        this.setRange(original.getRange());
    }

    public ScheduleEntry(Station station) {
        this();
        this.station = station;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and(min >= 0 && min <= max, "Invalid min/max range").and(ALLOWED_TYPES.contains(type), "Unknown type");
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public IntRange getRange() {
        return new IntRange(min, max);
    }

    @Override
    public void setRange(IntRange range) {
        min = range.getFrom();
        max = range.getTo();
    }
}
