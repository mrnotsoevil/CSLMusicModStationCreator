package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.model.IntRange;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PreciseIntRangeEditor extends HBox {

    private BooleanProperty resilientProperty;

    private Spinner<Integer> fromSpinner;

    private Spinner<Integer> toSpinner;

    private IntRange target;

    public PreciseIntRangeEditor() {
        resilientProperty = new SimpleBooleanProperty(true);
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
            if(target != null) {
                target.setFrom(nnew);
                inverseIfNeeded();
            }
        });
        toSpinner.valueProperty().addListener((observableValue, old, nnew) -> {
            if(target != null) {
                target.setTo(nnew);
                inverseIfNeeded();
            }
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

    private void inverseIfNeeded() {
        if(resilientProperty.get()) {
            if(target.getFrom() > target.getTo()) {
                IntRange t = target;
                target = null;
                int x = t.getFrom();
                t.setFrom(t.getTo());
                t.setTo(x);
                fromSpinner.getValueFactory().setValue(t.getFrom());
                toSpinner.getValueFactory().setValue(t.getTo());
                target = t;
            }
        }
    }

    public IntRange getTarget() {
        return target;
    }

    public void setTarget(IntRange target, IntRange borders) {
        this.target = target;

        fromSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borders.getFrom(), borders.getTo(), target.getFrom()));
        toSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borders.getFrom(), borders.getTo(), target.getTo()));
    }

    public BooleanProperty getResilientProperty() {
        return resilientProperty;
    }

    public void setResilientProperty(BooleanProperty resilientProperty) {
        this.resilientProperty = resilientProperty;
    }

    public boolean getResilient() {
        return resilientProperty.get();
    }

    public void setResilient(boolean value) {
        resilientProperty.set(value);
    }
}
