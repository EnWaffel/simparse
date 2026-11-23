package de.enwaffel.simparse;

public enum NoteType {
    SINGLE('1'),
    HOLD('2'),
    ROLL('4'),
    TAIL('3'),
    MINE('M'),
    LIFT('L'),
    FAKE('F'),
    KEY_SOUND('K');

    private final char c;

    NoteType(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    public static NoteType fromChar(char c) {
        for (NoteType type : values()) {
            if (type.c == c) return type;
        }

        return null;
    }

}
