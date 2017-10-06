package cslmusicmod.stationeditor.model;

public class IntRange implements Validatable {
    private int from;
    private int to;

    public IntRange() {

    }

    public IntRange(int from, int to ) {
        this.from = from;
        this.to = to;
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

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and( to <= from, "Invalid from/to range");
    }

    public ValidationResult isValid(int from, int to) {
        return isValid().and(this.from >= from && this.to <= to, "Only values from " + from + " to " + to + " allowed");
    }
}
