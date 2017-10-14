package cslmusicmod.stationeditor.model;

import java.util.*;
import java.util.stream.Collectors;

public class Formula implements Validatable {

    private transient ContextEntry context;

    private List<Conjunction> conjunctions;

    // Only used during deserialization
    private transient Map<String, ContextCondition> inlinedContextConditions;

    public Formula() {
        conjunctions = new ArrayList<>();
        inlinedContextConditions = new HashMap<>();
    }

    public Formula(ContextEntry context) {
        this();
        this.context = context;
    }

    public Formula(Formula original, ContextEntry parent) {
        this.context = parent;
        conjunctions = original.conjunctions.stream().map(x -> new Conjunction(x, this)).collect(Collectors.toList());
    }

    public List<Conjunction> getConjunctions() {
        return conjunctions;
    }

    public void setConjunctions(List<Conjunction> conjunctions) {
        this.conjunctions = conjunctions;
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
        return conjunctions.stream().map(x -> "(" + x.getSummary() + ")").collect(Collectors.joining(" or "));
    }

    public boolean isEmpty() {
        return conjunctions.isEmpty();
    }
}
