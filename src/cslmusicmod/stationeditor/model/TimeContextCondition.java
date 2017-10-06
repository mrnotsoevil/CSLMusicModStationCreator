package cslmusicmod.stationeditor.model;

import java.sql.Time;

public class TimeContextCondition extends ContextCondition {

    private int from;
    private int to;
    private boolean not;

    public TimeContextCondition() {
        from = 0;
        to = 24;
        not = false;
    }

    @Override
    public String getType() {
        return "time";
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
}

