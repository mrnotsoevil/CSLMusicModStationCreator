package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.model.IntRange;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PreciseIntRangeEditor extends HBox {

    private Spinner<Integer> fromSpinner;

    private Spinner<Integer> toSpinner;

    private IntRange target;

    public PreciseIntRangeEditor() {
        fromSpinner = new Spinner<>();
        toSpinner = new Spinner<>();

        getChildren().add(new Text("From"));
        getChildren().add(fromSpinner);
        getChildren().add(new Text("To"));
        getChildren().add(toSpinner);

        setSpacing(8);
        setAlignment(Pos.CENTER_LEFT);

        fromSpinner.setEditable(true);
        toSpinner.setEditable(true);

        fromSpinner.valueProperty().addListener((observableValue, old, nnew) -> {
            target.setFrom(nnew);
        });
        toSpinner.valueProperty().addListener((observableValue, old, nnew) -> {
            target.setTo(nnew);
        });

        // Javafx Bullshit
        fromSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fromSpinner.increment(0); // won't change value, but will commit editor
            }
        });
        toSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                toSpinner.increment(0); // won't change value, but will commit editor
            }
        });
    }

    public IntRange getTarget() {
        return target;
    }

    public void setTarget(IntRange target, IntRange borders) {
        this.target = target;

        fromSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borders.getFrom(), borders.getTo(), target.getFrom()));
        toSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borders.getFrom(), borders.getTo(), target.getTo()));
    }

}
