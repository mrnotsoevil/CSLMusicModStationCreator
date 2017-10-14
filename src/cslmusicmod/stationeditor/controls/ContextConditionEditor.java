package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.DialogHelper;
import cslmusicmod.stationeditor.model.ContextCondition;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;

public abstract class ContextConditionEditor extends BorderPane {

    protected String nameForCondition(ContextCondition condition) {
        if(condition.getStation().getFilters().values().contains(condition)) {
            return condition.getStation().getFilterName(condition);
        }
        else {
            return "";
        }
    }

    protected boolean canCreateOrRenameCondition(ContextCondition condition, StringProperty conditionName) {
        String name = conditionName.get().trim();

        if(name.isEmpty()) {
            DialogHelper.showErrorAlert("Condition name", "The name must be non-empty!");
            conditionName.set(nameForCondition(condition));
            return false;
        }
        if(condition.getStation().getFilters().values().contains(condition)) {
            if(!condition.getStation().getFilterName(condition).equals(name)) {
                if(condition.getStation().getFilters().keySet().contains(name)) {
                    DialogHelper.showErrorAlert("Rename condition", "The name must be unique!");
                    conditionName.set(nameForCondition(condition));

                    return false;
                }
            }
        }
        else {
            if(condition.getStation().getFilters().keySet().contains(name)) {
                DialogHelper.showErrorAlert("New condition", "The name must be unique!");
                conditionName.set(nameForCondition(condition));

                return false;
            }
        }


        return true;
    }

    protected void createOrRenameCondition(ContextCondition condition, StringProperty conditionName) {
        String name = conditionName.get().trim();

        if(condition.getStation().getFilters().values().contains(condition)) {
            condition.getStation().renameFilter(condition.getStation().getFilterName(condition), name);
        }
        else {
            condition.getStation().addFilter(name, condition);
        }
    }

}
