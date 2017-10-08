package cslmusicmod.stationeditor.model;

public abstract class ContextCondition implements Validatable {

    private transient Station station;

    private static int NAME_GENERATOR_COUNTER = 1;

    public abstract String getType();

    public abstract String getSummary();

    public static String generateUniqueName(ContextCondition cond) {
        return cond.getType() + NAME_GENERATOR_COUNTER++;
    }

    public ContextCondition() {

    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
