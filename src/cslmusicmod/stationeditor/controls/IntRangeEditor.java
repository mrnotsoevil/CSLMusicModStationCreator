package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.IntRange;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.RangeSlider;

import java.io.File;

public class IntRangeEditor extends HBox {

    private IntRange target;

    @FXML
    private Spinner<Integer> exactFrom;

    @FXML
    private Spinner<Integer> exactTo;

    @FXML
    private RangeSlider slider;

    public IntRangeEditor() {
        ControlsHelper.initControl(this);

        exactFrom.setEditable(true);
        exactTo.setEditable(true);

        exactFrom.valueProperty().addListener((observableValue, old, nnew) -> {
            if(getTarget() != null  && getTarget().getFrom() != nnew) {
                target.setFrom(nnew);
                slider.setLowValue(nnew);
            }
        });
        exactTo.valueProperty().addListener((observableValue, old, nnew) -> {
            if(getTarget() != null && getTarget().getTo() != nnew) {
                target.setTo(nnew);
                slider.setHighValue(nnew);
            }
        });

        slider.lowValueProperty().addListener((change) -> {
            int nnew = slider.lowValueProperty().intValue();
            if(target != null && getTarget().getFrom() != nnew) {
                target.setFrom(nnew);
                exactFrom.getValueFactory().setValue(nnew);
            }
        });
        slider.highValueProperty().addListener((change) -> {
            int nnew = slider.highValueProperty().intValue();
            if(target != null && getTarget().getTo() != nnew) {
                target.setTo(nnew);
                exactTo.getValueFactory().setValue(nnew);
            }
        });

        // Javafx Bullshit
        exactFrom.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                exactFrom.increment(0); // won't change value, but will commit editor
            }
        });
        exactTo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                exactTo.increment(0); // won't change value, but will commit editor
            }
        });
    }

    public IntRange getTarget() {
        return target;
    }

    public void setTarget(IntRange target, IntRange borders) {

        this.target = null;
        exactFrom.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borders.getFrom(), borders.getTo(), target.getFrom()));
        exactTo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borders.getFrom(), borders.getTo(), target.getTo()));
        slider.minProperty().set(borders.getFrom());
        slider.maxProperty().set(borders.getTo());
        slider.lowValueProperty().setValue(target.getFrom());
        slider.highValueProperty().setValue(target.getTo());
        slider.lowValueProperty().setValue(target.getFrom());
        slider.highValueProperty().setValue(target.getTo());
        this.target = target;
    }

    public void setMajorTickUnit(int unit) {
        slider.setMajorTickUnit(unit);
    }

    public void setMinorTickCount(int count) {
        slider.setMinorTickCount(count);
    }
}
