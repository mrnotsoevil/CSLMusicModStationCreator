package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.ContextConditionDNF;
import cslmusicmod.stationeditor.model.ContextEntry;
import cslmusicmod.stationeditor.model.Station;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.GridView;
import org.controlsfx.glyphfont.Glyph;

import java.util.ArrayList;
import java.util.List;

public class DNFEditor extends BorderPane {

    private ContextConditionDNF dnf;

    @FXML
    private GridView<List<String>> content;

    public DNFEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        content.getItems().add(new ArrayList<>());
        content.getItems().add(new ArrayList<>());
        content.getItems().add(new ArrayList<>());
        content.getItems().add(new ArrayList<>());
    }

    @FXML
    private void addNewEntry() {

    }

    @FXML
    private void removeEntries() {

    }
}
