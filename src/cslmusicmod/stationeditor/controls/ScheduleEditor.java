package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.TriggerEditCell;
import cslmusicmod.stationeditor.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.RangeSlider;

public class ScheduleEditor extends BorderPane {

    private Station station;

    @FXML
    private TableView<ScheduleEntry> content;

    @FXML
    private TableColumn<ScheduleEntry, String> contentTypeColumn;

    @FXML
    private TableColumn<ScheduleEntry, ScheduleEntry> contentTypeEditColumn;

    @FXML
    private TableColumn<ScheduleEntry, IntRange> contentNumberColumn;

    @FXML
    private TableColumn<ScheduleEntry, ScheduleEntry> contentNumberEditColumn;

    @FXML
    private ComboBox<String> newItemType;

    @FXML
    private PreciseIntRangeEditor newItemRange;

//    @FXML
//    private TableColumn<ScheduleEntry, ScheduleEntry> editColumn;

    public ScheduleEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    public void initialize() {

        ScheduleEntry.ALLOWED_TYPES.stream().forEach(x -> newItemType.getItems().add(x));
        newItemType.getSelectionModel().select(0);
        newItemRange.setTarget(new IntRange(0, 3), ScheduleEntry.RANGE_BORDERS);

        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        contentTypeColumn.setSortable(false);
        contentTypeColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>("Play " + value.getValue().getType());
        });
        contentTypeColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));
        contentTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<ScheduleEntry, String> t) -> {
            ScheduleEntry e = t.getRowValue();
            e.setType(t.getNewValue());
            t.getTableView().refresh();
        });
        contentNumberColumn.setSortable(false);
        contentNumberColumn.setCellValueFactory((value) -> {
            return new ReadOnlyObjectWrapper<>(new IntRange(value.getValue().getMin(), value.getValue().getMax()));
        });
        contentNumberColumn.setCellFactory(value -> new DurationTableCell());
        contentNumberColumn.setOnEditCommit((TableColumn.CellEditEvent<ScheduleEntry, IntRange> t) -> {
            ScheduleEntry e = t.getRowValue();
            e.setMin(t.getNewValue().getFrom());
            e.setMax(t.getNewValue().getTo());
            t.getTableView().refresh();
        });
        content.setRowFactory(ControlsHelper.dragDropReorderRowFactory(content));

        contentTypeEditColumn.setCellFactory(value -> new TriggerEditCell<>(contentTypeColumn));
        contentNumberEditColumn.setCellFactory(value -> new TriggerEditCell<>(contentNumberColumn));

//        Callback<TableColumn<ScheduleEntry, ScheduleEntry>, TableCell<ScheduleEntry, ScheduleEntry>> editCellFactory
//                = (TableColumn<ScheduleEntry, ScheduleEntry> p) -> new EditTableCell(contentNumberColumn, contentTypeColumn);
//        editColumn.setCellValueFactory((value) -> {
//            return new ReadOnlyObjectWrapper<>(value.getValue());
//        });
//        editColumn.setSortable(false);
//        editColumn.setCellFactory(editCellFactory);
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

    private static class DurationTableCell extends TableCell<ScheduleEntry, IntRange> {

        private PreciseIntRangeEditor slider;

        public DurationTableCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createSlider();
                setText(null);
                setGraphic(slider);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem().getFrom() + " - " + getItem().getTo() + " songs");
            setGraphic(null);
        }

        @Override
        public void updateItem(IntRange item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (slider != null) {
                        slider.setTarget(new IntRange(item), ScheduleEntry.RANGE_BORDERS);
                    }
                    setText(null);
                    setGraphic(slider);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createSlider() {
            slider = new PreciseIntRangeEditor();
            slider.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            slider.focusedProperty().addListener(
                    (ObservableValue<? extends Boolean> arg0,
                     Boolean arg1, Boolean arg2) -> {
                        System.out.println(arg2);
                        if (!arg2) {
                            commitEdit(new IntRange(slider.getTarget()));
                        }
                    });
            slider.setTarget(new IntRange(getItem()), ScheduleEntry.RANGE_BORDERS);
        }

        private String getString() {
            return getItem() == null ? "" : getItem().getFrom() + " - " + getItem().getTo() + " songs";
        }
    }
}
