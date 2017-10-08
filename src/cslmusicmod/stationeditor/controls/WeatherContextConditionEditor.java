package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.IntRange;
import cslmusicmod.stationeditor.model.ValidationResult;
import cslmusicmod.stationeditor.model.WeatherContextCondition;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class WeatherContextConditionEditor extends BorderPane {

    private WeatherContextCondition condition;

    @FXML
    private TextField conditionName;

    @FXML
    private CheckBox invertCondition;

    @FXML
    private IntRangeEditor temparatureRange;

    @FXML
    private IntRangeEditor rainRange;

    @FXML
    private IntRangeEditor cloudRange;

    @FXML
    private IntRangeEditor fogRange;

    @FXML
    private IntRangeEditor rainbowRange;

    @FXML
    private IntRangeEditor northernLightsRange;


    public WeatherContextConditionEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        temparatureRange.setMajorTickUnit(10);
        temparatureRange.setMinorTickCount(10);
    }

    public WeatherContextCondition getCondition() {
        return condition;
    }

    private WeatherContextCondition writeTo(WeatherContextCondition target) {

        target.setTemperature(temparatureRange.getTarget());
        target.setRain(rainRange.getTarget());
        target.setCloudy(cloudRange.getTarget());
        target.setFoggy(fogRange.getTarget());
        target.setRainbow(rainbowRange.getTarget());
        target.setNorthernlights(northernLightsRange.getTarget());
        target.setNot(invertCondition.isSelected());

        return target;
    }

    public void setCondition(WeatherContextCondition condition) {
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
        temparatureRange.setTarget(new IntRange(condition.getTemperature()), WeatherContextCondition.TEMPERATURE_RANGE_BORDERS);
        rainRange.setTarget(new IntRange(condition.getRain()), WeatherContextCondition.WEATHER_RANGE_BORDERS);
        cloudRange.setTarget(new IntRange(condition.getCloudy()), WeatherContextCondition.WEATHER_RANGE_BORDERS);
        fogRange.setTarget(new IntRange(condition.getFoggy()), WeatherContextCondition.WEATHER_RANGE_BORDERS);
        rainbowRange.setTarget(new IntRange(condition.getRainbow()), WeatherContextCondition.WEATHER_RANGE_BORDERS);
        northernLightsRange.setTarget(new IntRange(condition.getNorthernlights()), WeatherContextCondition.WEATHER_RANGE_BORDERS);
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

        ValidationResult validation = writeTo(new WeatherContextCondition(condition)).isValid();

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
