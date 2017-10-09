package cslmusicmod.stationeditor.model;

import java.util.*;
import java.util.stream.Collectors;

public class ContextConditionDNF implements Validatable {

    private transient ContextEntry context;

    private List<List<String>> dnf;

    // Only used during deserialization
    private transient Map<String, ContextCondition> inlinedContextConditions;

    public ContextConditionDNF() {
        dnf = new ArrayList<>();
        inlinedContextConditions = new HashMap<>();
    }

    public ContextConditionDNF(ContextEntry context) {
        this();
        this.context = context;
    }

    public ContextConditionDNF(ContextConditionDNF original) {
        this.context = original.context;
        dnf = original.dnf.stream().map(x -> new ArrayList<String>(x)).collect(Collectors.toList());
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

    public ContextEntry getContext() {
        return context;
    }

    public void setContext(ContextEntry context) {
        this.context = context;
    }

    public String getSummary() {
        return dnf.stream().map(x -> "(" + x.stream().collect(Collectors.joining(" and ")) + ")").collect(Collectors.joining(" or "));
    }

    public boolean isEmpty() {
        return dnf.isEmpty();
    }
}
