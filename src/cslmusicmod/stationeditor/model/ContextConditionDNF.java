package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextConditionDNF implements Validatable {

    private List<List<String>> dnf;

    private Map<String, ContextCondition> inlinedContextConditions;

    public ContextConditionDNF() {
        dnf = new ArrayList<>();
        inlinedContextConditions = new HashMap<>();
    }

    public List<List<String>> getDnf() {
        return dnf;
    }

    public void setDnf(List<List<String>> dnf) {
        this.dnf = dnf;
    }

    public Map<String, ContextCondition> getInlinedContextConditions() {
        return inlinedContextConditions;
    }

    public void setInlinedContextConditions(Map<String, ContextCondition> inlinedContextConditions) {
        this.inlinedContextConditions = inlinedContextConditions;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this);
    }
}
