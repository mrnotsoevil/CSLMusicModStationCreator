package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conjunction {

    private Formula formula;

    private List<String> literals;

    public Conjunction() {
        literals = new ArrayList<>();
    }

    public Conjunction(Formula formula) {
        this();
        this.formula = formula;
    }

    public Conjunction(Conjunction original, Formula parent) {
        this.formula = parent;
        this.literals = new ArrayList<>(original.literals);
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public void setLiterals(List<String> literals) {
        this.literals = literals;
    }

    public String getSummary() {
        return literals.stream().collect(Collectors.joining(" and "));
    }
}
