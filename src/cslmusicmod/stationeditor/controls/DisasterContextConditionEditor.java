package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.DisasterContextCondition;
import cslmusicmod.stationeditor.model.IntRange;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckModel;
import org.controlsfx.control.IndexedCheckModel;

import java.util.stream.Collectors;

public class DisasterContextConditionEditor extends ContextConditionEditor {

    private DisasterContextCondition condition;

    @FXML
    private TextField conditionName;

    @FXML
    private CheckBox invertCondition;

    @FXML
    private PreciseIntRangeEditor disasterCount;

    @FXML
    private CheckComboBox<String> disasterFilter;

    public DisasterContextConditionEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        DisasterContextCondition.KNOWN_DISASTERS.stream().forEach(x -> disasterFilter.getItems().add(x));
    }

    public DisasterContextCondition getCondition() {
        return condition;
    }

    private DisasterContextCondition writeTo(DisasterContextCondition target) {

        target.setRange(disasterCount.getTarget());
        target.setNot(invertCondition.isSelected());
        target.setOf(disasterFilter.getCheckModel().getCheckedItems());

        return target;
    }

    public void setCondition(DisasterContextCondition condition) {
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
        disasterCount.setTarget(new IntRange(condition.getRange()), DisasterContextCondition.RANGE_BORDERS);
    }

    @FXML
    private void saveData() {

        if(!canCreateOrRenameCondition(condition, conditionName.textProperty())) {
            return;
        }

        ValidationResult validation = writeTo(new DisasterContextCondition(condition, condition.getStation())).isValid();

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
