package cslmusicmod.stationeditor.controls.helpers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TriggerRowEditCell <K,V> extends EditCell<K,V>  {

    public TriggerRowEditCell() {
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(!isEmpty())
            ((EditRow)getTableRow()).edit();
    }

}
