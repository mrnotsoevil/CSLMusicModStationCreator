package cslmusicmod.stationeditor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private  Validatable creator;

    private List<Problem> problems;

    public ValidationResult(Validatable creator) {
        this.problems = new ArrayList<>();
        this.creator = creator;
    }

    public ValidationResult and(boolean cond, String description) {

        if(!cond) {
            problems.add(new Problem(creator, description));
        }

        return this;
    }

    public ValidationResult and(Validatable test) {
        problems.addAll(test.isValid().problems);
        return this;
    }

    public ValidationResult and(Collection<? extends Validatable> test) {
        test.forEach(x -> problems.addAll(x.isValid().problems));
        return this;
    }

    public ValidationResult andResults(Collection<ValidationResult> results) {
        results.forEach(x -> problems.addAll(x.problems));
        return this;
    }

    public boolean isOK() {
        return problems.isEmpty();
    }

    public List<Problem> getProblems() {
        return Collections.unmodifiableList(problems);
    }

    public static class Problem {
        private Validatable source;
        private String description;

        public Problem(Validatable source, String description) {
            this.source = source;
            this.description = description;
        }

        public Validatable getSource() {
            return source;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", source.getClass().getSimpleName(), description);
        }
    }

}
