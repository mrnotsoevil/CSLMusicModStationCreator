package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.EditCell;
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

public class ContextsEditor extends BorderPane {

    private Station station;

    @FXML
    private TableView<ContextEntry> content;

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

        Callback<TableColumn<ContextEntry, ContextEntry>, TableCell<ContextEntry, ContextEntry>> editCellFactory
                = (TableColumn<ContextEntry, ContextEntry> p) -> new EditTableCell();

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
        editColumn.setCellFactory(editCellFactory);
        content.setRowFactory(ControlsHelper.dragDropReorderRowFactory(content));

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

        for(ContextEntry todelete : content.getSelectionModel().getSelectedItems()) {
            station.getContexts().remove(todelete);
        }
    }

    private static class EditTableCell extends EditCell<ContextEntry, ContextEntry> {

        private Button editButton;

        public EditTableCell() {

        }

        @Override
        public void handle(ActionEvent actionEvent) {
            ContextEntryEditor dlg = new ContextEntryEditor();
            Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Edit context");
            dlg.setContextEntry(getItem());
            stage.showAndWait();
            getTableView().refresh();
        }
    }
}
