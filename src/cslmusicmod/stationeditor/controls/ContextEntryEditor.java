package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.ContextEntry;
import cslmusicmod.stationeditor.model.Formula;
import cslmusicmod.stationeditor.model.SongCollection;
import cslmusicmod.stationeditor.model.ValidationResult;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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

    @FXML
    private FiltersEditor filtersEditor;

    public ContextEntryEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {

        songs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        contextCollections.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                updateSongsAutoComplete();
            }
        });
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
        entry.getStation().getCollections().values().forEach(x -> contextCollections.getItems().add(x.getName()));
        filtersEditor.setStation(condition.getStation());

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
        updateSongsAutoComplete();
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
            songToAdd.setText("");
        }
    }

    @FXML
    private void removeSongs() {
        songs.getItems().removeAll(songs.getSelectionModel().getSelectedItems());
    }

    private void updateSongsAutoComplete() {

        if(entry == null)
            return;

        Set<String> complete = new HashSet<>();

        for(String name : contextCollections.getCheckModel().getCheckedItems())  {
            SongCollection coll = entry.getStation().getCollections().get(name);
            coll.getLocalListOfSongs().stream().forEach(x -> complete.add(x.getName()));
        }

        TextFields.bindAutoCompletion(songToAdd, complete);
    }
}
