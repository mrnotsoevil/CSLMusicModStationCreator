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

    public IntRange scale(int scale) {
        return new IntRange(from * scale, to * scale);
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and( to <= from, "Invalid from/to range");
    }

    public ValidationResult isValid(int from, int to) {
        return isValid().and(this.from >= from && this.to <= to, "Only values from " + from + " to " + to + " allowed");
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IntRange) {
            return from == ((IntRange)o).from && to == ((IntRange)o).to;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%d-%d", from, to);
    }

    public String toString(String unit) {
        return String.format("%d%s-%d%s", from, unit, to, unit);
    }
}
