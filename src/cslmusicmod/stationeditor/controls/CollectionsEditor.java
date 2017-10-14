package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.EditCell;
import cslmusicmod.stationeditor.controls.helpers.TriggerEditCell;
import cslmusicmod.stationeditor.model.ScheduleEntry;
import cslmusicmod.stationeditor.model.Station;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class CollectionsEditor extends BorderPane {

    private Station station;

    private TextInputDialog addItemDialog;

    @FXML
    private TableView<String> content;

    @FXML
    private TableColumn<String, String> nameColumn;

    @FXML
    private TableColumn<String, String> editColumn;

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
            return new ReadOnlyObjectWrapper<>(value.getValue());
        });
        editColumn.setCellFactory(value -> new CollectionsEditCell());

    }

    private void connectData() {
        ObservableList<String> data = FXCollections.observableArrayList(station.getCollections());
        content.setItems(data);
        station.setCollections(data);
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

            if(!content.getItems().contains(name)) {
                content.getItems().add(name);
            }
        }
    }

    @FXML
    private void removeEntries() {
        content.getItems().removeAll(content.getSelectionModel().getSelectedItems());
    }

    private static class CollectionsEditCell extends EditCell<String, String> {

        @Override
        public void handle(ActionEvent actionEvent) {

        }
    }

}
