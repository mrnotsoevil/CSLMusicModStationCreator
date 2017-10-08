package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.IntRange;
import cslmusicmod.stationeditor.model.TimeContextCondition;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.util.stream.Collectors;

public class TimeContextConditionEditor extends BorderPane {

    private TimeContextCondition condition;

    @FXML
    private TextField conditionName;

    @FXML
    private CheckBox invertCondition;

    @FXML
    private PreciseIntRangeEditor timeRange;

    @FXML
    private CheckComboBox<String> disasterFilter;

    public TimeContextConditionEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
    }

    public TimeContextCondition getCondition() {
        return condition;
    }

    private TimeContextCondition writeTo(TimeContextCondition target) {

        target.setRange(timeRange.getTarget());
        target.setNot(invertCondition.isSelected());

        return target;
    }

    public void setCondition(TimeContextCondition condition) {
        this.condition = condition;
        revertData();
    }

    @FXML
    private void closeWindow() {
        ((Stage)this.getScene().getWindow()).close();
    }

    @FXML
    private void revertData() {
        conditionName.setText(condition.getStation().getFilterName(condition));
        invertCondition.setSelected(condition.isNot());
        timeRange.setTarget(new IntRange(condition.getRange()), TimeContextCondition.RANGE_BORDERS);
    }

    @FXML
    private void saveData() {

        String name = conditionName.getText().trim();

        if(name.isEmpty()) {
            DialogHelper.showErrorAlert("Rename condition", "The name must be non-empty!");
            conditionName.setText(condition.getStation().getFilterName(condition));
            return;
        }
        if(!condition.getStation().getFilterName(condition).equals(name)) {
            if(condition.getStation().getFilters().keySet().contains(name)) {
                DialogHelper.showErrorAlert("Rename condition", "The name must be unique!");
                conditionName.setText(condition.getStation().getFilterName(condition));

                return;
            }
        }

        ValidationResult validation = writeTo(new TimeContextCondition(condition)).isValid();

        if(validation.isOK()) {
            condition.getStation().renameFilter(condition.getStation().getFilterName(condition), name);
            writeTo(condition);

            closeWindow();
        }
        else {
            DialogHelper.showErrorAlert("Invalid data", validation.getProblems().stream().map( (ValidationResult.Problem x) -> x.toString()).collect(Collectors.joining("\n")));
        }
    }
}