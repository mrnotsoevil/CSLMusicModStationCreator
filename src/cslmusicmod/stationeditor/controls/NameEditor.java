package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.model.Station;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NameEditor extends TextField {

    private Station station;

    public NameEditor() {
        textProperty().addListener(changeEvent -> {
            station.setName(textProperty().getValue());
        });
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;

        textProperty().setValue(station.getName());
    }
}
