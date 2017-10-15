package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.EditCell;
import cslmusicmod.stationeditor.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Collections;

public class ScheduleEditor extends BorderPane {

    private Station station;

    @FXML
    private TableView<ScheduleEntry> content;

    @FXML
    private TableColumn<ScheduleEntry, Integer> contentPriorityColumn;

    @FXML
    private TableColumn<ScheduleEntry, String> contentTypeColumn;

    @FXML
    private TableColumn<ScheduleEntry, IntRange> contentNumberColumn;

    @FXML
    private TableColumn<ScheduleEntry, ScheduleEntry> editColumn;

    @FXML
    private ComboBox<String> newItemType;

    @FXML
    private PreciseIntRangeEditor newItemRange;


    public ScheduleEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    public void initialize() {

        ScheduleEntry.ALLOWED_TYPES.stream().forEach(x -> newItemType.getItems().add(x));
        newItemType.getSelectionModel().select(0);
        newItemRange.setTarget(new IntRange(0, 3), ScheduleEntry.RANGE_BORDERS);

        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        content.setEditable(false);

        contentPriorityColumn.setSortable(false);
        contentPriorityColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getStation().getSchedule().indexOf(value.getValue()) + 1));

        contentTypeColumn.setSortable(false);
        contentTypeColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>("Play " + value.getValue().getType());
        });
        contentTypeColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));
        contentNumberColumn.setSortable(false);
        contentNumberColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(new IntRange(value.getValue().getMin(), value.getValue().getMax()));
        });

        editColumn.setCellFactory(value -> new ScheduleEntryEditCell());
        editColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue()));


        content.setRowFactory(ControlsHelper.dragDropReorderRowFactory(content));
        content.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DELETE) {
                removeEntries();
            }
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
        ScheduleEntry e = new ScheduleEntry(station);
        e.setRange(new IntRange(newItemRange.getTarget()));
        e.setType(newItemType.getValue());
        content.getItems().add(e);
    }

    @FXML
    private void removeEntries() {
        content.getItems().removeAll(content.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void moveEntryUp() {
        int index = content.getSelectionModel().getFocusedIndex();

        if(index >= 1) {
            Collections.swap(content.getItems(), index, index - 1);
            content.getSelectionModel().select(index - 1);
        }
    }

    @FXML
    private void moveEntryDown() {
        int index = content.getSelectionModel().getFocusedIndex();

        if(index >= 0 && index < content.getItems().size() - 1) {
            Collections.swap(content.getItems(), index, index + 1);
            content.getSelectionModel().select(index + 1);
        }
    }

    private static class ScheduleEntryEditCell extends EditCell<ScheduleEntry, ScheduleEntry> {

        @Override
        public void handle(ActionEvent actionEvent) {
            ScheduleEntryEditor editor = new ScheduleEntryEditor();
            Stage stage = ControlsHelper.createModalStageFor(this, editor, "Edit schedule entry");
            editor.setEntry(getItem());
            stage.showAndWait();
            getTableView().refresh();
        }
    }

}
