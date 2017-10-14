package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.glyphfont.Glyph;

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

    private static class EditTableCell extends TableCell<ContextEntry, ContextEntry> implements EventHandler<ActionEvent> {

        private Button editButton;

        public EditTableCell() {
            createButton();
        }

        @Override
        public void updateItem(ContextEntry item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(null);
                setGraphic(editButton);
            }
        }

        private void createButton() {
            editButton = new Button();
            editButton.setText("Edit");
            editButton.setPrefWidth(100);
            editButton.setOnAction(this);
            editButton.setGraphic(Glyph.create("FontAwesome|pencil"));
        }

        private String getString() {
            return "";
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