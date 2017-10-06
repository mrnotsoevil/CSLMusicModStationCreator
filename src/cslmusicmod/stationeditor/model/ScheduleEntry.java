package cslmusicmod.stationeditor.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScheduleEntry implements Validatable {

    public static List<String> ALLOWED_TYPES = Collections.unmodifiableList(Arrays.asList("music",
            "blurb",
            "broadcast",
            "commercial",
            "talk"));

    private String type;
    private int min;
    private int max;

    public ScheduleEntry() {
        type = "music";
        min = 0;
        max = 3;
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
}
