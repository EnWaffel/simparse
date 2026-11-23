package de.enwaffel.simparse;

abstract class ParserBase {

    private String content = "";
    protected int pos = 0;

    protected void setupParser(String content) {
        this.content = content;
        pos = 0;
    }

    protected char next() {
        return content.charAt(pos++);
    }

    protected char peek() {
        return content.charAt(pos);
    }

    protected void skip(int count) {
        while (count-- > 0) pos++;
    }

    protected String until(char end) {
        StringBuilder builder = new StringBuilder();

        while (pos < content.length() && peek() != end) {
            builder.append(next());
        }

        return builder.toString();
    }

    protected int contentLength() {
        return content.length();
    }

}
