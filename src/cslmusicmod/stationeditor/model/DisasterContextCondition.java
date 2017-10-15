package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DisasterContextCondition extends ContextCondition implements IntRanged {

    public static List<String> KNOWN_DISASTERS = Collections.unmodifiableList(Arrays.asList("Structure Fire",
            "Structure Collapse",
            "Generic Flood",
            "Meteor Strike",
            "Tsunami",
            "Forest Fire",
            "Earthquake",
            "Tornado",
            "Thunderstorm",
            "Sinkhole",
            "Chirpynado"));
    public static IntRange RANGE_BORDERS = new IntRange(0, 256);
    public static IntRange DEFAULT_RANGE = RANGE_BORDERS;

    private String type = "disaster";
    private int from;
    private int to;
    private List<String> of;
    private boolean not;

    public DisasterContextCondition() {
        from = 0;
        to = 256;
        of = Collections.emptyList();
        not = false;
    }

    public DisasterContextCondition(Station station) {
        this();
        setStation(station);
    }

    public DisasterContextCondition(DisasterContextCondition original, Station parent) {
        this.setStation(parent);
        this.setRange(original.getRange());
        this.of = new ArrayList<>(original.of);
        this.not = original.not;
    }

    @Override
    public String getType() {
        return "disaster";
    }

    @Override
    public String getSummary() {
        return (not ? "Not " : "") + String.format("%d to %d disasters of %s", from, to, of.isEmpty() ? "all disaster types" : of.stream().collect(Collectors.joining(" or ")));
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<String> getOf() {
        return of;
    }

    public void setOf(List<String> of) {
        this.of = of;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and(from >= 0 && to <= 256 && from <= to, "Invalid from/to range").
                and(of.stream().allMatch(x -> KNOWN_DISASTERS.contains(x)), "Unknown disaster types");
    }

    @Override
    public IntRange getRange() {
        return new IntRange(from, to);
    }

    @Override
    public void setRange(IntRange range) {
        from = range.getFrom();
        to = range.getTo();
    }
}
