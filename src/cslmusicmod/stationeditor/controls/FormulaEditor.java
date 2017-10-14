package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.Conjunction;
import cslmusicmod.stationeditor.model.Formula;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.GridView;

public class FormulaEditor extends BorderPane {

    private Formula dnf;

    @FXML
    private GridView<Conjunction> content;

    public FormulaEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        content.setCellFactory(value -> new ConjunctionEditor());
        content.setCellWidth(200);
        content.setCellHeight(200);
    }

    @FXML
    private void addNewEntry() {
        content.getItems().add(new Conjunction(dnf));
    }


    public void setDnf(Formula dnf) {
        this.dnf = dnf;
        content.setItems(FXCollections.observableArrayList(dnf.getConjunctions()));
        dnf.setConjunctions(content.getItems());
    }

    public Formula getDnf() {
        return dnf;
    }
}
