package cslmusicmod.stationeditor.model;

import java.sql.Time;

public class TimeContextCondition extends ContextCondition implements IntRanged {

    public static IntRange RANGE_BORDERS = new IntRange(0, 24);
    public static IntRange DEFAULT_RANGE = RANGE_BORDERS;

    private int from;
    private int to;
    private boolean not;

    public TimeContextCondition() {
        from = 0;
        to = 24;
        not = false;
    }

    public TimeContextCondition(Station station) {
        this();
        setStation(station);
    }

    public TimeContextCondition(TimeContextCondition original, Station parent) {
        this.setStation(parent);
        this.setRange(original.getRange());
        this.not = original.not;
    }

    @Override
    public String getType() {
        return "time";
    }

    @Override
    public String getSummary() {
        return (not ? "Not " : "") + String.format("From %dh to %dh", from, to);
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

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and(from >= 0 && from <= 24 && to >= 0 && to <= 24, "Must range from 0 to 24");
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

