package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.IntRange;
import cslmusicmod.stationeditor.model.MoodContextCondition;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.util.stream.Collectors;

public class MoodContextConditionEditor extends ContextConditionEditor {

    private MoodContextCondition condition;

    @FXML
    private TextField conditionName;

    @FXML
    private CheckBox invertCondition;

    @FXML
    private IntRangeEditor happinessRange;

    @FXML
    private CheckComboBox<String> disasterFilter;

    public MoodContextConditionEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        happinessRange.setMajorTickUnit(10);
        happinessRange.setMinorTickCount(10);
    }

    public MoodContextCondition getCondition() {
        return condition;
    }

    private MoodContextCondition writeTo(MoodContextCondition target) {

        target.setRange(happinessRange.getTarget());
        target.setNot(invertCondition.isSelected());

        return target;
    }

    public void setCondition(MoodContextCondition condition) {
        this.condition = condition;
        revertData();
    }

    @FXML
    private void closeWindow() {
        ((Stage)this.getScene().getWindow()).close();
    }

    @FXML
    private void revertData() {
        conditionName.setText(nameForCondition(condition));
        invertCondition.setSelected(condition.isNot());
        happinessRange.setTarget(new IntRange(condition.getRange()), MoodContextCondition.RANGE_BORDERS);
    }

    @FXML
    private void saveData() {

        if(!canCreateOrRenameCondition(condition, conditionName.textProperty())) {
            return;
        }

        ValidationResult validation = writeTo(new MoodContextCondition(condition, condition.getStation())).isValid();

        if(validation.isOK()) {
            createOrRenameCondition(condition, conditionName.textProperty());
            writeTo(condition);

            closeWindow();
        }
        else {
            DialogHelper.showValidationError("Invalid data", "The entered data is not valid!", validation);
        }
    }
}
