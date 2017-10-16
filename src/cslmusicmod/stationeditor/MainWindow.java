package cslmusicmod.stationeditor;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import cslmusicmod.stationeditor.controls.*;
import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.controls.helpers.RememberingFileChooser;
import cslmusicmod.stationeditor.helpers.CitiesHelper;
import cslmusicmod.stationeditor.helpers.CopyTask;
import cslmusicmod.stationeditor.helpers.DesktopHelper;
import cslmusicmod.stationeditor.helpers.FileHelper;
import cslmusicmod.stationeditor.model.Station;
import cslmusicmod.stationeditor.model.ValidationResult;
import cslmusicmod.stationeditor.model.VanillaCollections;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestException;
import java.util.List;
import java.util.Optional;
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

//    @FXML
//    private FiltersEditor filtersEditor;

    @FXML
    private ContextsEditor contextsEditor;

    @FXML
    private ProgressBar progress;

    private RememberingFileChooser stationFileChooser;

    private DirectoryChooser exportDirectoryChooser;

    public MainWindow() {
        station = new Station();
        stationFileChooser = new RememberingFileChooser();
        stationFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*")
        );
        exportDirectoryChooser = new DirectoryChooser();
        exportDirectoryChooser.setTitle("Export music pack");
    }

    @FXML
    private void initialize() {

        progress.managedProperty().bind(progress.visibleProperty());
        progress.setVisible(false);

        updateEditors();
        updateTitle();
    }

    private void updateEditors() {
        thumbnailEditor.setStation(station);
        nameEditor.setStation(station);
        descriptionEditor.setStation(station);
        scheduleEditor.setStation(station);
        collectionsEditor.setStation(station);
//        filtersEditor.setStation(station);
        contextsEditor.setStation(station);
    }

    @FXML
    public void openFile() {
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
                e.printStackTrace();
                DialogHelper.showExceptionError("Open station", "Error while loading the data!", e);
            }
        }
    }

    @FXML
    public boolean saveFile() {

        ValidationResult validation = station.isValid();

        if(!validation.isOK()) {
            DialogHelper.showValidationError("Save station", "The data is not valid!", validation);
            return false;
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
                return false;
            }
            finally {
                updateTitle();
            }

            return true;
        }

        return false;
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    @FXML
    public void newFile() {

        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Create a new station?",
                ButtonType.YES,
                ButtonType.NO
        );
        closeConfirmation.setHeaderText("New station");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(root.getScene().getWindow());

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.YES.equals(closeResponse.get())) {
            station = new Station();
            updateEditors();
            updateTitle();
        }
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

    private void exportStation(File target) {

        if(!station.hasSaveLocation()) {
            DialogHelper.showErrorAlert("Export music pack", "You need to save the station once.");
            return;
        }

        if(!saveFile()){
            return;
        }

        try {
            List<File> export = station.getExportableFiles();

            Path exportroot = target.toPath();
            Path sourceroot = exportroot.resolve("Source");
            Path stationroot = exportroot.resolve("CSLMusicMod_Music");

            if(!Files.exists(sourceroot)) {
                Files.createDirectory(sourceroot);
            }
            if(!Files.exists(stationroot)) {
                Files.createDirectory(stationroot);
            }

            Files.write(sourceroot.resolve("Mod.cs"), station.buildModSource().getBytes(Charsets.UTF_8), StandardOpenOption.CREATE);

            Path stationdefinitionroot = Paths.get(station.getDirectory());

            CopyTask task = new CopyTask(export.stream().map(file -> {
                return new CopyTask.Entry(file, stationroot.resolve(stationdefinitionroot.relativize(file.toPath())).toFile());
            }).collect(Collectors.toList()));
            task.runningProperty().addListener((observableValue, aBoolean, t1) -> {
                root.setDisable(t1);
                progress.setVisible(t1);
            });
            progress.progressProperty().unbind();
            progress.progressProperty().bind(task.progressProperty());
            new Thread(task).start();

        } catch (IOException e) {
            DialogHelper.showExceptionError("Export music pack", "Error while exporting the music pack!", e);
        }
    }

    @FXML
    public void exportStation() {

        File target = exportDirectoryChooser.showDialog(root.getScene().getWindow());

        if(target != null) {
            exportStation(target);
        }
    }

    @FXML
    public void exportStationToLocalModFolder() {
        Path folder = Paths.get(CitiesHelper.getLocalModFolder()).resolve(FileHelper.sanatizeFilename(station.getName()));
        if(!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        exportStation(folder.toFile());
    }

    @FXML
    public void openDocumentation() {
        DesktopHelper.browser(URI.create("https://gitlab.com/rumangerst/cslmusicmod-station-editor/wikis/Overview"));
    }

    @FXML
    public void reportIssue() {
        DesktopHelper.browser(URI.create("https://gitlab.com/rumangerst/cslmusicmod-station-editor/issues"));
    }

    @FXML
    public void showAbout() {
        About dlg = new About();
        Stage stage = ControlsHelper.createModalStageFor(root, dlg, "About");
        stage.showAndWait();
    }

    @FXML
    public void openLocalModFolder() {
        File folder = new File(CitiesHelper.getLocalModFolder());
        if(!folder.exists()) {
            try {
                Files.createDirectories(folder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DesktopHelper.open(folder);
    }
}
