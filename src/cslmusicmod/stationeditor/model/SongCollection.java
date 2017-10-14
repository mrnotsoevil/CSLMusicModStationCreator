package cslmusicmod.stationeditor.model;

public class SongCollection implements Validatable {

    private Station station;

    private String name;

    public SongCollection() {
        name = "";
    }

    public SongCollection(Station station) {
        this();
        this.station = station;
    }

    public SongCollection(SongCollection original, Station parent) {
        this.name = original.name;
        this.station = parent;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this).and(!name.trim().isEmpty(), "Name cannot be empty!");
    }
}
