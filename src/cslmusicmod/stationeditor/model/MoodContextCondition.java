package cslmusicmod.stationeditor.model;

public class MoodContextCondition extends ContextCondition {
    private int from;
    private int to;
    private boolean not;

    public MoodContextCondition() {
        from = 0;
        to = 100;
        not = false;
    }

    @Override
    public String getType() {
        return "mood";
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
        return new ValidationResult(this).and(to <= from && to >= 0 && from <= 100, "Mood must range from 0 to 100");
    }
}
