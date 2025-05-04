package io.beanvortex.bitkip.models;

public enum TurnOffMode {
    NOTHING,
    SLEEP,
    TURN_OFF;

    @Override
    public String toString() {
        return this.name();
    }

    public static TurnOffMode getValue(String value) {
        if (value == null
                || value.isBlank()
                || value.equals("null")) {
            return NOTHING; //default
        }
        return TurnOffMode.valueOf(value);
    }
}
