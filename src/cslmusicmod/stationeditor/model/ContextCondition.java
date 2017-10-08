package cslmusicmod.stationeditor.model;

public abstract class ContextCondition implements Validatable {

    private static int NAME_GENERATOR_COUNTER = 1;

    public abstract String getType();

    public abstract String getSummary();

    public static String generateUniqueName(ContextCondition cond) {
        return cond.getType() + NAME_GENERATOR_COUNTER++;
    }
}
