package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.EditCell;
import cslmusicmod.stationeditor.controls.helpers.EditRow;
import cslmusicmod.stationeditor.controls.helpers.TriggerRowEditCell;
import cslmusicmod.stationeditor.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Collections;

public class ContextsEditor extends BorderPane {

    private Station station;

    @FXML
    private TableView<ContextEntry> content;

    @FXML
    private TableColumn<ContextEntry, Integer> contentPriorityColumn;

    @FXML
    private TableColumn<ContextEntry, String> nameColumn;

    @FXML
    private TableColumn<ContextEntry, String> summaryColumn;

    @FXML
    private TableColumn<ContextEntry, ContextEntry> editColumn;

    public ContextsEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        contentPriorityColumn.setSortable(false);
        contentPriorityColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getStation().getContexts().indexOf(value.getValue()) + 1));

        nameColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue().getName());
        });
        summaryColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue().getSummary());
        });
        editColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue());
        });
        editColumn.setSortable(false);
        editColumn.setCellFactory(value -> new TriggerRowEditCell<>());
        content.setRowFactory(ControlsHelper.dragDropReorderRowFactory(content, new EditTableRow()));

        content.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DELETE) {
                removeEntries();
            }
        });
    }

    private void connectData() {
        ObservableList<ContextEntry> data = FXCollections.observableArrayList(station.getContexts());
        content.setItems(data);
        station.setContexts(data);
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
        connectData();
    }

   @FXML
   private void addEntry() {
       ContextEntryEditor dlg = new ContextEntryEditor();
       Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Create context");
       dlg.setContextEntry(new ContextEntry(station));
       stage.showAndWait();
       connectData();
   }

    @FXML
    private void removeEntries() {
        station.getContexts().removeAll(content.getSelectionModel().getSelectedItems());
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

    public static class EditTableRow extends EditRow<ContextEntry> {

        @Override
        public void edit() {
            ContextEntryEditor dlg = new ContextEntryEditor();
            Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Edit context");
            dlg.setContextEntry(getItem());
            stage.showAndWait();
            getTableView().refresh();
        }
    }
}
