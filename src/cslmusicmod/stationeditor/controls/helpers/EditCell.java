package cslmusicmod.stationeditor.controls.helpers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import org.controlsfx.glyphfont.Glyph;

public abstract class EditCell<K,V> extends TableCell<K, V> implements EventHandler<ActionEvent>  {

    private Button editButton;

    public EditCell() {
        createButton();
    }

    @Override
    public void updateItem(V item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            setGraphic(editButton);
        }
    }

    private void createButton() {
        editButton = new Button();
        editButton.setText("Edit");
        editButton.setPrefWidth(100);
        editButton.setOnAction(this);
        editButton.setGraphic(Glyph.create("FontAwesome|pencil"));
    }

    private String getString() {
        return "";
    }

}
