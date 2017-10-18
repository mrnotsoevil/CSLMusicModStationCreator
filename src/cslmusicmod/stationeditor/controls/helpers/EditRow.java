package cslmusicmod.stationeditor.controls.helpers;

import javafx.scene.control.TableRow;

public abstract class EditRow<T> extends TableRow<T> {

    public EditRow() {

        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (! isEmpty()) ) {
                edit();
            }
        });
    }

    public abstract void edit();

}
