package de.enwaffel.simparse;

public enum StepsType {
    DANCE_SINGLE("dance-single"),
    DANCE_DOUBLE("dance-double"),
    DANCE_SOLO("dance-solo"),
    PUMP_SINGLE("pump-single");

    private final String name;

    StepsType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static StepsType fromName(String name) {
        for (StepsType stepsType : values()) {
            if (stepsType.name.equals(name)) return stepsType;
        }

        return null;
    }

}
