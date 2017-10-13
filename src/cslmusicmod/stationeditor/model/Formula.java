package cslmusicmod.stationeditor.model;

import java.util.*;
import java.util.stream.Collectors;

public class Formula implements Validatable {

    private transient ContextEntry context;

    private List<Conjunction> dnf;

    // Only used during deserialization
    private transient Map<String, ContextCondition> inlinedContextConditions;

    public Formula() {
        dnf = new ArrayList<>();
        inlinedContextConditions = new HashMap<>();
    }

    public Formula(ContextEntry context) {
        this();
        this.context = context;
    }

    public Formula(Formula original) {
        this.context = original.context;
        dnf = original.dnf.stream().map(x -> new Conjunction(x)).collect(Collectors.toList());
    }

    public List<Conjunction> getDnf() {
        return dnf;
    }

    public void setDnf(List<Conjunction> dnf) {
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
        return dnf.stream().map(x -> "(" + x.getSummary() + ")").collect(Collectors.joining(" or "));
    }

    public boolean isEmpty() {
        return dnf.isEmpty();
    }
}
