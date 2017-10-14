package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.ContextEntry;
import cslmusicmod.stationeditor.model.Formula;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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

    @FXML
    private ListView<String> songs;

    @FXML
    private TextField songToAdd;

    public ContextEntryEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {

        songs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private ContextEntry writeTo(ContextEntry target) {

        target.setName(contextName.getText());
        target.setCollections(new ArrayList<>(contextCollections.getCheckModel().getCheckedItems()));
        target.setConditions(formulaEditor.getDnf());
        target.setSongs(new ArrayList<>(songs.getItems()));

        return target;
    }

    public void setContextEntry(ContextEntry condition) {
        this.entry = condition;
        contextCollections.getItems().clear();
        entry.getStation().getCollections().forEach(x -> contextCollections.getItems().add(x));

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
        formulaEditor.setDnf(new Formula(entry.getConditions(), entry));
        songs.setItems(FXCollections.observableArrayList(entry.getSongs()));
    }

    @FXML
    private void saveData() {

        ValidationResult validation = writeTo(new ContextEntry(entry, entry.getStation())).isValid();

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

    @FXML
    private void addSong() {
        String s = songToAdd.getText().trim();

        if(!s.isEmpty() && !songs.getItems().contains(s) ) {
            songs.getItems().add(s);
        }
    }

    @FXML
    private void removeSongs() {
        songs.getItems().removeAll(songs.getSelectionModel().getSelectedItems());
    }
}
