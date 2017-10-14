package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.Conjunction;
import cslmusicmod.stationeditor.model.Station;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import org.controlsfx.control.GridCell;

import java.util.Collections;

public class ConjunctionEditor extends GridCell<Conjunction> {

    private Conjunction conjunction;

    @FXML
    private ListView<String> content;

    @FXML
    private MenuButton addEntry;

    public ConjunctionEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;

        Station station = conjunction.getFormula().getContext().getStation();

        addEntry.getItems().clear();
        for(String id : station.getFilters().keySet()) {
            MenuItem item = new MenuItem();
            item.setText(id);

            item.setOnAction(event -> {
                if(!content.getItems().contains(id)) {
                    content.getItems().add(id);
                }
            });

            addEntry.getItems().add(item);
        }

        content.setItems(FXCollections.observableArrayList(conjunction.getLiterals()));
        conjunction.setLiterals(content.getItems());
    }

    @Override
    protected void updateItem(Conjunction item, boolean empty) {
        super.updateItem(item, empty);

        if(!empty) {
            setConjunction(item);
        }
        else {
            content.setItems(FXCollections.observableArrayList(Collections.emptyList()));
        }
    }

    @FXML
    private void removeEntries() {
        content.getItems().removeAll(content.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void deleteConjunction() {
        conjunction.getFormula().getConjunctions().remove(conjunction);
    }
}
