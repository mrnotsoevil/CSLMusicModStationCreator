package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conjunction {

    private Formula dnf;

    private List<String> literals;

    public Conjunction() {
        literals = new ArrayList<>();
    }

    public Conjunction(Formula dnf) {
        this();
        this.dnf = dnf;
    }

    public Conjunction(Conjunction original) {
        this.dnf = original.dnf;
        this.literals = new ArrayList<>(original.literals);
    }

    public Formula getDnf() {
        return dnf;
    }

    public void setDnf(Formula dnf) {
        this.dnf = dnf;
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
