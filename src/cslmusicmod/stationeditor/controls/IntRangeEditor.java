package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.model.IntRange;
import javafx.fxml.FXML;
import org.controlsfx.control.RangeSlider;

public class IntRangeEditor extends RangeSlider {

    private IntRange target;

    public IntRangeEditor() {
        setShowTickLabels(true);
        setShowTickMarks(true);
        setMin(0);
        setMax(256);
        setMinorTickCount(0);
        setMajorTickUnit(1);
        setSnapToTicks(true);

        lowValueProperty().addListener((change) -> {
            if(target != null) {
                target.setFrom(lowValueProperty().intValue());
            }
        });
        highValueProperty().addListener((change) -> {
            if(target != null) {
                target.setFrom(highValueProperty().intValue());
            }
        });
    }

    public IntRange getTarget() {
        return target;
    }

    public void setTarget(IntRange target) {
        this.target = target;
        this.lowValueProperty().setValue(target.getFrom());
        this.highValueProperty().setValue(target.getTo());
    }
}
