package cslmusicmod.stationeditor;

import com.google.gson.Gson;
import cslmusicmod.stationeditor.controls.*;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.Station;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class MainWindow {

    private static Gson stationSerializer = Station.getGson();

    private Station station;

    @FXML
    private Node root;

    @FXML
    private ThumbnailEditor thumbnailEditor;

    @FXML
    private NameEditor nameEditor;

    @FXML
    private DescriptionEditor descriptionEditor;

    @FXML
    private ScheduleEditor scheduleEditor;

    @FXML
    private CollectionsEditor collectionsEditor;

    @FXML
    private FiltersEditor filtersEditor;

    @FXML
    private ContextsEditor contextsEditor;

    private FileChooser openStationChooser;

    public MainWindow() {
        station = new Station();
        openStationChooser = new FileChooser();
        openStationChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
    }

    @FXML
    private void initialize() {
        updateEditors();
    }

    private void updateEditors() {
        thumbnailEditor.setStation(station);
        nameEditor.setStation(station);
        descriptionEditor.setStation(station);
        scheduleEditor.setStation(station);
        collectionsEditor.setStation(station);
        filtersEditor.setStation(station);
        contextsEditor.setStation(station);
    }

    public void openFile(ActionEvent actionEvent) {
        File target = openStationChooser.showOpenDialog(root.getScene().getWindow());

        if(target != null) {
            try (FileReader r = new FileReader(target)) {
                Station loaded = stationSerializer.fromJson(r, Station.class);
                ValidationResult result = loaded.isValid();

                if(!result.isOK()) {
                    DialogHelper.showValidationError("Open station", "The loaded data is not valid!", result);
                    return;
                }

                loaded.setDirectory(target.getParent());
                station = loaded;
                updateEditors();

            } catch (Exception e) {
                DialogHelper.showExceptionError("Open station", "Error while loading the data!", e);
            }
        }
    }

    public void saveFile(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void newFile(ActionEvent actionEvent) {
        station = new Station();
        updateEditors();
    }
}
