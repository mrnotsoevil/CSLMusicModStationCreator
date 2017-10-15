package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.controls.helpers.TriggerEditCell;
import cslmusicmod.stationeditor.helpers.CopyToDirectoryTask;
import cslmusicmod.stationeditor.model.ScheduleEntry;
import cslmusicmod.stationeditor.model.Song;
import cslmusicmod.stationeditor.model.SongCollection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SongCollectionEditor extends BorderPane {

    private SongCollection collection;

    private Path targetDirectory;

    @FXML
    private TableView<Song> content;

    @FXML
    private TableColumn<Song, String> contentNameColumn;

    @FXML
    private TableColumn<Song, String> contentTypeColumn;

    @FXML
    private TableColumn<Song, Song> contentTypeEditColumn;

    @FXML
    private ProgressBar progress;

    private FileChooser songFileChooser;

    public SongCollectionEditor() {
        ControlsHelper.initControl(this);

        songFileChooser = new FileChooser();
        songFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OGG Vorbis (*.ogg)", "*.ogg")
        );
    }

    @FXML
    private void initialize() {

        content.setEditable(true);
        contentNameColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getName()));
        contentNameColumn.setEditable(false);
        contentTypeColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getSongType()));
        contentTypeColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));
        contentTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<Song, String> t) -> {
            Song e = t.getRowValue();
            e.setSongType(t.getNewValue());
            refreshContent();
        });
        contentTypeEditColumn.setCellFactory(value -> new TriggerEditCell<>(contentTypeColumn));
        progress.managedProperty().bind(progress.visibleProperty());
        progress.setVisible(false);
        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        content.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DELETE) {
                removeContent();
            }
        });
    }

    public SongCollection getCollection() {
        return collection;
    }

    @FXML
    private void refreshContent() {
        content.getItems().clear();

        try {
            Files.list(targetDirectory).forEach(path -> {
                String extension = com.google.common.io.Files.getFileExtension(path.toString()).toLowerCase();

                if(extension.equals("ogg")) {
                    content.getItems().add(new Song(path.toString()));
                }
            });
        } catch (IOException e) {
            DialogHelper.showExceptionError("Collection editor", "Error while refreshing the list", e);
        }
    }

    @FXML
    private void addContent() {
        List<File> toadd = songFileChooser.showOpenMultipleDialog(getScene().getWindow());

        if(toadd != null && !toadd.isEmpty()) {
            addContent(toadd);
        }
    }

    private void addContent(List<File> sources) {
        CopyToDirectoryTask task = new CopyToDirectoryTask(sources, targetDirectory);
        task.runningProperty().addListener((observableValue, aBoolean, t1) -> {
            setDisable(t1);
            progress.setVisible(t1);
            refreshContent();
        });
        progress.progressProperty().unbind();
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    @FXML
    private void removeContent() {
        List<Song> selected = content.getSelectionModel().getSelectedItems();

        for(Song song : selected) {
            song.delete();
        }

        refreshContent();
    }

    public void setCollection(SongCollection collection) {
        this.collection = collection;

        targetDirectory = Paths.get(collection.getStation().getDirectory(), collection.getName());

        if(!Files.exists(targetDirectory)) {
            try {
                Files.createDirectory(targetDirectory);
            } catch (IOException e) {
                DialogHelper.showExceptionError("Collection editor", "Error while creating the necessary directory", e);
                setDisable(true);
                return;
            }
        }

        refreshContent();
    }

}
