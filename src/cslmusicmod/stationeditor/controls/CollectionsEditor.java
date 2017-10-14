package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.controls.helpers.EditCell;
import cslmusicmod.stationeditor.controls.helpers.TriggerEditCell;
import cslmusicmod.stationeditor.model.ScheduleEntry;
import cslmusicmod.stationeditor.model.SongCollection;
import cslmusicmod.stationeditor.model.Station;
import cslmusicmod.stationeditor.model.VanillaCollections;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class CollectionsEditor extends BorderPane {

    private Station station;

    private TextInputDialog addItemDialog;

    @FXML
    private TableView<SongCollection> content;

    @FXML
    private TableColumn<SongCollection, String> nameColumn;

    @FXML
    private TableColumn<SongCollection, SongCollection> editColumn;

    @FXML
    private SplitMenuButton addNewEntry;

    public CollectionsEditor() {
        ControlsHelper.initControl(this);

        addItemDialog = new TextInputDialog();
        addItemDialog.setHeaderText("Add collection");
        addItemDialog.setContentText("Set the name of the collection");
    }

    @FXML
    private void initialize() {

        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue().getName());
        });
        editColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue()));
        editColumn.setCellFactory(value -> new CollectionsEditCell());

        for(SongCollection coll : VanillaCollections.getInstance().getCollections()) {
            MenuItem item = new MenuItem();
            item.setText(coll.getName());
            item.setOnAction(event -> {
                addNewEntry(coll.getName());
            });
            addNewEntry.getItems().add(item);
        }

    }

    private void connectData() {
        ObservableList<SongCollection> data = FXCollections.observableArrayList(station.getCollections().values());
        content.setItems(data);
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
        connectData();
    }

    @FXML
    private void addNewEntry() {
        addItemDialog.getEditor().textProperty().setValue("");
        Optional<String> result = addItemDialog.showAndWait();

        if(result.isPresent()) {

            String name = result.get().trim();
            addNewEntry(name);

        }
    }

    private void addNewEntry(String name) {
        if(!station.getCollections().keySet().contains(name)) {
            SongCollection coll = new SongCollection(station);
            coll.setName(name);
            coll.setStation(station);
            station.addCollection(coll);
            connectData();
        }
    }

    @FXML
    private void removeEntries() {
       connectData();
    }

    private static class CollectionsEditCell extends EditCell<SongCollection, SongCollection> {

        @Override
        public void handle(ActionEvent actionEvent) {
            SongCollection coll = getItem();

            if(!coll.getStation().hasSaveLocation()) {
                DialogHelper.showErrorAlert("Song collection editor", "Please save the station once to enable the editor.");
                return;
            }
        }
    }

}
