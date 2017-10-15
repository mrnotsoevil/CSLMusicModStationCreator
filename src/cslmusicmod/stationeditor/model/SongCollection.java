package cslmusicmod.stationeditor.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean isEditable() {
        return VanillaCollections.getInstance().getCollections().stream().allMatch(x -> !x.getName().equals(getName()));
    }

    public List<Song> getLocalListOfSongs() {
        try {
            return Files.list(Paths.get(station.getDirectory(), name)).filter(path -> {
                String extension = com.google.common.io.Files.getFileExtension(path.toString()).toLowerCase();
                return extension.equals("ogg");
            }).map(path -> new Song(path.toString())).collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
