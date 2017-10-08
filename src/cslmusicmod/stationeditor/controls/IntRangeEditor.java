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
        setMax(10);
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
                target.setTo(highValueProperty().intValue());
            }
        });
    }

    public IntRange getTarget() {
        return target;
    }

    public void setTarget(IntRange target, IntRange borders) {
        this.target = null;
        this.minProperty().set(borders.getFrom());
        this.maxProperty().set(borders.getTo());
        this.lowValueProperty().setValue(target.getFrom());
        this.highValueProperty().setValue(target.getTo());
        this.lowValueProperty().setValue(target.getFrom());
        this.highValueProperty().setValue(target.getTo());
        this.target = target;
    }
}
