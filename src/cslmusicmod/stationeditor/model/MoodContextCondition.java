package cslmusicmod.stationeditor.model;

public class MoodContextCondition extends ContextCondition implements IntRanged {

    public static IntRange RANGE_BORDERS = new IntRange(0, 100);
    public static IntRange DEFAULT_RANGE = RANGE_BORDERS;

    private int from;
    private int to;
    private boolean not;

    public MoodContextCondition() {
        from = 0;
        to = 100;
        not = false;
    }

    public MoodContextCondition(Station station) {
        this();
        setStation(station);
    }

    public MoodContextCondition(MoodContextCondition original) {
        this.setStation(original.getStation());
        this.setRange(original.getRange());
        this.not = original.not;
    }

    @Override
    public String getType() {
        return "mood";
    }

    @Override
    public String getSummary() {
        return (not ? "Not " : "") + String.format("Mood from %d to %d", from, to);
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
        return new ValidationResult(this).and(from <= to && from >= 0 && to <= 100, "Mood must range from 0 to 100");
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
