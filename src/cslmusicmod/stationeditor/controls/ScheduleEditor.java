package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.IntRange;
import cslmusicmod.stationeditor.model.ScheduleEntry;
import cslmusicmod.stationeditor.model.Station;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.control.RangeSlider;

public class ScheduleEditor extends BorderPane {

    private Station station;

    @FXML
    private TableView<ScheduleEntry> content;

    @FXML
    private TableColumn<ScheduleEntry, String> contentTypeColumn;

    @FXML
    private TableColumn<ScheduleEntry, IntRange> contentNumberColumn;

    public ScheduleEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    public void initialize() {

        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Callback<TableColumn<ScheduleEntry, IntRange>, TableCell<ScheduleEntry, IntRange>> durationCellFactory
                = (TableColumn<ScheduleEntry, IntRange> p) -> new DurationTableCell();

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
        contentNumberColumn.setCellFactory(durationCellFactory);
        contentNumberColumn.setOnEditCommit((TableColumn.CellEditEvent<ScheduleEntry, IntRange> t) -> {
            ScheduleEntry e = t.getRowValue();
            e.setMin(t.getNewValue().getFrom());
            e.setMax(t.getNewValue().getTo());
            t.getTableView().refresh();
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
        content.getItems().add(e);
    }

    @FXML
    private void removeEntries() {
        content.getItems().removeAll(content.getSelectionModel().getSelectedItems());
    }

    private static class DurationTableCell extends TableCell<ScheduleEntry, IntRange> {

        private RangeSlider slider;

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
                        slider.setLowValue(item.getFrom());
                        slider.setHighValue(item.getTo());
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
            slider = new RangeSlider();
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMin(0);
            slider.setMax(10);
            slider.setMinorTickCount(0);
            slider.setLowValue(getItem().getFrom());
            slider.setHighValue(getItem().getTo());
            slider.setMajorTickUnit(1);
            slider.setSnapToTicks(true);
            slider.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            slider.focusedProperty().addListener(
                    (ObservableValue<? extends Boolean> arg0,
                     Boolean arg1, Boolean arg2) -> {
                        if (!arg2) {
                            commitEdit(new IntRange((int) Math.round(slider.getLowValue()), (int) Math.round(slider.getHighValue())));
                        }
                    });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().getFrom() + " - " + getItem().getTo() + " songs";
        }
    }
}
