package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.*;
import cslmusicmod.stationeditor.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FiltersEditor extends BorderPane {

    private Station station;

    private TextInputDialog addItemDialog;

    @FXML
    private TableView<ContextCondition> content;

    @FXML
    private TableColumn<ContextCondition, String> nameColumn;

    @FXML
    private TableColumn<ContextCondition, String> summaryColumn;

    @FXML
    private TableColumn<ContextCondition, ContextCondition> editColumn;

    public FiltersEditor() {
        ControlsHelper.initControl(this);

        addItemDialog = new TextInputDialog();
        addItemDialog.setHeaderText("Add condition");
        addItemDialog.setContentText("Set the name of the condition");
    }

    @FXML
    private void initialize() {
        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        content.setRowFactory(value -> new EditTableRow());

        nameColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(station.getFilterName(value.getValue()));
        });
        summaryColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue().getSummary());
        });
        editColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(value.getValue());
        });
        editColumn.setSortable(false);
        editColumn.setCellFactory(value -> new TriggerRowEditCell<>());
    }

    private void connectData() {
        ObservableList<ContextCondition> data = FXCollections.observableArrayList(station.getFilters().values());
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
    private void addDisasterCondition() {
        DisasterContextConditionEditor dlg = new DisasterContextConditionEditor();
        Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Create condition");
        dlg.setCondition(new DisasterContextCondition(station));
        stage.showAndWait();
        connectData();
    }

    @FXML
    private void addMoodCondition() {
        MoodContextConditionEditor dlg = new MoodContextConditionEditor();
        Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Create condition");
        dlg.setCondition(new MoodContextCondition(station));
        stage.showAndWait();
        connectData();
    }

    @FXML
    private void addTimeCondition() {
        TimeContextConditionEditor dlg = new TimeContextConditionEditor();
        Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Create condition");
        dlg.setCondition(new TimeContextCondition(station));
        stage.showAndWait();
        connectData();
    }

    @FXML
    private void addWeatherCondition() {
        WeatherContextConditionEditor dlg = new WeatherContextConditionEditor();
        Stage stage = ControlsHelper.createModalStageFor(this, dlg, "Create condition");
        dlg.setCondition(new WeatherContextCondition(station));
        stage.showAndWait();
        connectData();
    }

    @FXML
    private void removeEntries() {

        for(ContextCondition todelete : content.getSelectionModel().getSelectedItems()) {
            if(!station.canRemoveFilter(station.getFilterName(todelete))) {
                DialogHelper.showErrorAlert("Delete filter", "The filter " + station.getFilterName(todelete) + " is being used! Cannot delete it!");
                return;
            }
        }

        for(ContextCondition todelete : content.getSelectionModel().getSelectedItems()) {
            station.removeFilter(station.getFilterName(todelete));
        }

        connectData();
    }

    private static class EditTableRow extends EditRow<ContextCondition> {


        @Override
        public void edit() {
            if(getItem() instanceof DisasterContextCondition) {
                DisasterContextConditionEditor editor = new DisasterContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor, "Edit condition");
                editor.setCondition((DisasterContextCondition)getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else if(getItem() instanceof MoodContextCondition) {
                MoodContextConditionEditor editor = new MoodContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor, "Edit condition");
                editor.setCondition((MoodContextCondition) getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else if(getItem() instanceof TimeContextCondition) {
                TimeContextConditionEditor editor = new TimeContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor, "Edit condition");
                editor.setCondition((TimeContextCondition) getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else if(getItem() instanceof WeatherContextCondition) {
                WeatherContextConditionEditor editor = new WeatherContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor, "Edit condition");
                editor.setCondition((WeatherContextCondition) getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else {
                throw new RuntimeException("Unknown context condition " + getItem().getClass().getSimpleName());
            }
        }
    }
}
