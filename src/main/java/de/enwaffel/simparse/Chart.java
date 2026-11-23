package de.enwaffel.simparse;

import java.util.List;

public class Chart {

    private final StepsType type;
    private final String description;
    private final String difficulty;
    private final int meter;
    private final List<Note> notes;

    Chart(StepsType type, String description, String difficulty, int meter, List<Note> notes) {
        this.type = type;
        this.description = description;
        this.difficulty = difficulty;
        this.meter = meter;
        this.notes = notes;
    }

    public StepsType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getMeter() {
        return meter;
    }

    public List<Note> getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "type=" + type +
                ", description='" + description + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", meter=" + meter +
                ", notes=" + notes +
                '}';
    }

}
