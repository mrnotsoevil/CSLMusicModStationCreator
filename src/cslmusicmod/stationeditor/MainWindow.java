package cslmusicmod.stationeditor;

import com.google.gson.Gson;
import cslmusicmod.stationeditor.controls.*;
import cslmusicmod.stationeditor.model.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileReader;
import java.io.IOException;

public class MainWindow {

    private Station station;

    @FXML
    private ThumbnailEditor thumbnailEditor;

    @FXML
    private NameEditor nameEditor;

    @FXML
    private ScheduleEditor scheduleEditor;

    @FXML
    private CollectionsEditor collectionsEditor;

    @FXML
    private FiltersEditor filtersEditor;

    @FXML
    private ContextsEditor contextsEditor;

    public MainWindow() {
        Gson gson = Station.getGson();
        try(FileReader r = new FileReader("TestStation.json")) {
            station = gson.fromJson(r, Station.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        thumbnailEditor.setStation(station);
        nameEditor.setStation(station);
        scheduleEditor.setStation(station);
        collectionsEditor.setStation(station);
        filtersEditor.setStation(station);
        contextsEditor.setStation(station);
    }

}
