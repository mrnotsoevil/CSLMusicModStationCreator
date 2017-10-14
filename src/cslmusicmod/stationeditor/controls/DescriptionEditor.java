package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.model.Station;
import javafx.scene.control.TextField;

public class DescriptionEditor extends TextField {

    private Station station;

    public DescriptionEditor() {
        textProperty().addListener(changeEvent -> {
            station.setName(textProperty().getValue());
        });
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;

        textProperty().setValue(station.getDescription());
    }
}
