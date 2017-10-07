package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.ScheduleEntry;
import cslmusicmod.stationeditor.model.Station;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ScheduleEditor extends BorderPane {

    private Station station;

    @FXML
    private TableView<ScheduleEntry> content;

    @FXML
    private TableColumn<ScheduleEntry, String> contentTypeColumn;

    @FXML
    private TableColumn<ScheduleEntry, String> contentNumberColumn;

    public ScheduleEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    public void initialize() {

        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        contentTypeColumn.setSortable(false);
        contentTypeColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>("Play " + value.getValue().getType());
        });
        contentTypeColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));
        contentTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<ScheduleEntry, String> t) -> {
            ScheduleEntry e = t.getRowValue();
            e.setType(t.getNewValue());

        });
        contentNumberColumn.setSortable(false);
        contentNumberColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue().getMin() + " - " + value.getValue().getMax() + " times");
        });
    }

    private void connectData() {
        ObservableList<ScheduleEntry> data = FXCollections.observableArrayList(station.getSchedule());
        content.setItems(data);
        station.setSchedule(data);
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
        ScheduleEntry e = new ScheduleEntry();
        content.getItems().add(e);
    }

    @FXML
    private void removeEntries() {

    }
}
