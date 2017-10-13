package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.ContextEntry;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ContextEntryEditor extends BorderPane {

    private ContextEntry entry;

    @FXML
    private TextField contextName;

    @FXML
    private FormulaEditor formulaEditor;

    @FXML
    private CheckListView<String> contextCollections;

    public ContextEntryEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {


    }

    private ContextEntry writeTo(ContextEntry target) {

        target.setName(contextName.getText());
        target.setCollections(new ArrayList<>(contextCollections.getCheckModel().getCheckedItems()));

        return target;
    }

    public void setContextEntry(ContextEntry condition) {
        this.entry = condition;
        contextCollections.getItems().clear();
        entry.getStation().getCollections().forEach(x -> contextCollections.getItems().add(x));
        formulaEditor.setDnf(condition.getConditions());

        revertData();
    }

    @FXML
    private void closeWindow() {
        ((Stage)this.getScene().getWindow()).close();
    }

    @FXML
    private void revertData() {
        this.contextName.setText(entry.getName());
        this.contextCollections.getCheckModel().clearChecks();
        entry.getCollections().forEach(x -> contextCollections.getCheckModel().check(x));
    }

    @FXML
    private void saveData() {

        ValidationResult validation = writeTo(new ContextEntry(entry)).isValid();

        if(validation.isOK()) {

            if(!entry.getStation().getContexts().contains(entry)) {
                entry.getStation().getContexts().add(entry);
            }

            writeTo(entry);
            closeWindow();
        }
        else {
            DialogHelper.showErrorAlert("Invalid data", validation.getProblems().stream().map( (ValidationResult.Problem x) -> x.toString()).collect(Collectors.joining("\n")));
        }
    }
}
