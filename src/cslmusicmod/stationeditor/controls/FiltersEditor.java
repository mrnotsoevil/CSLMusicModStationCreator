package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
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

import java.util.Optional;

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

        Callback<TableColumn<ContextCondition, ContextCondition>, TableCell<ContextCondition, ContextCondition>> editCellFactory
                = (TableColumn<ContextCondition, ContextCondition> p) -> new EditTableCell();

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
        editColumn.setCellFactory(editCellFactory);
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

    private void addCondition(ContextCondition cond) {

        addItemDialog.getEditor().textProperty().setValue("");
        Optional<String> result = addItemDialog.showAndWait();

        if(result.isPresent()) {

            String name = result.get().trim();

            if(!name.isEmpty() && !station.getFilters().containsKey(result.get())) {
                station.getFilters().put(result.get(), cond);
                connectData();
            }
            else {
                DialogHelper.showErrorAlert("Add new condition", "The name must be unique and non-empty!");
            }
        }

    }

    @FXML
    private void addDisasterCondition() {
        addCondition(new DisasterContextCondition(station));
    }

    @FXML
    private void addMoodCondition() {
        addCondition(new MoodContextCondition(station));
    }

    @FXML
    private void addTimeCondition() {
        addCondition(new TimeContextCondition(station));
    }

    @FXML
    private void addWeatherCondition() {
        addCondition(new WeatherContextCondition(station));
    }

    @FXML
    private void removeEntries() {

        for(ContextCondition todelete : content.getSelectionModel().getSelectedItems()) {
            station.getFilters().remove(station.getFilterName(todelete));
        }

        connectData();
    }

    private static class EditTableCell extends TableCell<ContextCondition, ContextCondition> implements EventHandler<ActionEvent> {

        private Button editButton;

        public EditTableCell() {
            createButton();
        }

        @Override
        public void updateItem(ContextCondition item, boolean empty) {
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
        }

        private String getString() {
            return "";
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            if(getItem() instanceof DisasterContextCondition) {
                DisasterContextConditionEditor editor = new DisasterContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor);
                editor.setCondition((DisasterContextCondition)getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else if(getItem() instanceof MoodContextCondition) {
                MoodContextConditionEditor editor = new MoodContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor);
                editor.setCondition((MoodContextCondition) getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else if(getItem() instanceof TimeContextCondition) {
                TimeContextConditionEditor editor = new TimeContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor);
                editor.setCondition((TimeContextCondition) getItem());
                stage.showAndWait();
                getTableView().refresh();
            }
            else if(getItem() instanceof WeatherContextCondition) {
                WeatherContextConditionEditor editor = new WeatherContextConditionEditor();
                Stage stage = ControlsHelper.createModalStageFor(this, editor);
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
