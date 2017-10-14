package cslmusicmod.stationeditor.controls.helpers;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;

public class TriggerEditCell<K,V> extends EditCell<K,V> {

    private TableColumn column;

    public TriggerEditCell(TableColumn column) {
        this.column = column;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        getTableView().edit(getTableRow().getIndex(), column);
    }
}
