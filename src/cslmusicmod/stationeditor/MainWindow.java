package cslmusicmod.stationeditor;

import com.google.gson.Gson;
import cslmusicmod.stationeditor.controls.*;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.helpers.FileHelper;
import cslmusicmod.stationeditor.model.Station;
import cslmusicmod.stationeditor.model.ValidationResult;
import cslmusicmod.stationeditor.model.VanillaCollections;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.DigestException;

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

    private FileChooser stationFileChooser;

    private DirectoryChooser exportDirectoryChooser;

    public MainWindow() {
        station = new Station();
        stationFileChooser = new FileChooser();
        stationFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*")
        );
        exportDirectoryChooser = new DirectoryChooser();
    }

    @FXML
    private void initialize() {
        updateEditors();
        updateTitle();
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
        File target = stationFileChooser.showOpenDialog(root.getScene().getWindow());

        if(target != null) {
            try (FileReader r = new FileReader(target)) {
                Station loaded = stationSerializer.fromJson(r, Station.class);
                ValidationResult result = loaded.isValid();

                if(!result.isOK()) {
                    DialogHelper.showValidationError("Open station", "The loaded data is not valid!", result);
                    return;
                }

                loaded.setDirectory(target.getParent());
                loaded.setFilename(target.getAbsolutePath());
                station = loaded;
                updateEditors();
                updateTitle();

            } catch (Exception e) {
                DialogHelper.showExceptionError("Open station", "Error while loading the data!", e);
            }
        }
    }

    public void saveFile(ActionEvent actionEvent) {

        ValidationResult validation = station.isValid();

        if(!validation.isOK()) {
            DialogHelper.showValidationError("Save station", "The data is not valid!", validation);
            return;
        }

        File path;

        if(station.hasSaveLocation()) {
            path = new File(station.getFilename());
        }
        else {
            stationFileChooser.setInitialFileName(station.getName() + ".json");
            path = stationFileChooser.showSaveDialog(root.getScene().getWindow());
        }

        if(path != null) {

            // Save the thumbnail
            if(!station.getThumbnail().isEmpty()) {

                if(Paths.get(station.getThumbnail()).isAbsolute()) {

                    File targetfile = Paths.get(path.getParent(), FileHelper.sanatizeFilename(station.getName() + ".png")).toFile();
                    File srcfile = new File(station.getThumbnail());

                    try {
                        Thumbnails.of(srcfile).size(64, 64).keepAspectRatio(false).outputFormat("png").toFile(targetfile);
                    } catch (IOException e) {
                        DialogHelper.showExceptionError("Save station", "Error while saving the thumbnail!", e);
                    }

                    station.setThumbnail(targetfile.getName());
                    thumbnailEditor.setStation(station);
                }
                else if(!station.getThumbnail().toLowerCase().endsWith(".png") && station.hasSaveLocation()) {
                    File targetfile = Paths.get(path.getParent(), FileHelper.sanatizeFilename(station.getThumbnail() + ".png")).toFile();
                    File srcfile = Paths.get(path.getParent(), station.getThumbnail()).toFile();

                    try {
                        Thumbnails.of(srcfile).size(64, 64).keepAspectRatio(false).outputFormat("png").toFile(targetfile);
                    } catch (IOException e) {
                        DialogHelper.showExceptionError("Save station", "Error while saving the thumbnail!", e);
                    }

                    station.setThumbnail(targetfile.getName());
                    thumbnailEditor.setStation(station);
                }

            }

            try(FileWriter w = new FileWriter(path)) {
                w.write(stationSerializer.toJson(station));
                station.setFilename(path.getAbsolutePath());
                station.setDirectory(path.getParent());
            } catch (IOException e) {
                DialogHelper.showExceptionError("Save station", "Error while saving!", e);
            }
            finally {
                updateTitle();
            }


        }

    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void newFile(ActionEvent actionEvent) {
        station = new Station();
        updateEditors();
        updateTitle();
    }

    public void updateTitle() {

        try {
            if(station.hasSaveLocation()) {
                ((Stage)root.getScene().getWindow()).setTitle(new File(station.getFilename()).getName() + " - CSL Music Mod Station Editor");
            }
            else {
                ((Stage)root.getScene().getWindow()).setTitle("CSL Music Mod Station Editor");
            }
        }
        catch (Exception e) {

        }


    }
}
