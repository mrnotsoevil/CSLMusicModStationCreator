package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.IntRange;
import cslmusicmod.stationeditor.model.ScheduleEntry;
import cslmusicmod.stationeditor.model.TimeContextCondition;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

public class ScheduleEntryEditor extends ContextConditionEditor {

    private ScheduleEntry entry;

    @FXML
    private ComboBox<String> type;

    @FXML
    private PreciseIntRangeEditor duration;

    public ScheduleEntryEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        ScheduleEntry.ALLOWED_TYPES.stream().forEach(x -> type.getItems().add(x));
    }

    public ScheduleEntry getEntry() {
        return entry;
    }

    private ScheduleEntry writeTo(ScheduleEntry target) {

        target.setRange(duration.getTarget());
        target.setType(type.getValue());

        return target;
    }

    public void setEntry(ScheduleEntry entry) {
        this.entry = entry;
        revertData();
    }

    @FXML
    private void closeWindow() {
        ((Stage)this.getScene().getWindow()).close();
    }

    @FXML
    private void revertData() {
        type.getSelectionModel().select(entry.getType());
        duration.setTarget(new IntRange(entry.getRange()), ScheduleEntry.RANGE_BORDERS);
    }

    @FXML
    private void saveData() {

        ValidationResult validation = writeTo(new ScheduleEntry(entry, entry.getStation())).isValid();

        if(validation.isOK()) {
            writeTo(entry);
            closeWindow();
        }
        else {
            DialogHelper.showValidationError("Invalid data", "The entered data is not valid!", validation);
        }
    }
}
