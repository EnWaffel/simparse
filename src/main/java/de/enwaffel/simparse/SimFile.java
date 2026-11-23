package de.enwaffel.simparse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class SimFile {

    private final Map<String, String> header;
    private final List<Chart> charts;

    SimFile(Map<String, String> header, List<Chart> charts) {
        this.header = header;
        this.charts = charts;
    }

    public String getTitle() {
        return getHeaderValue("TITLE");
    }

    public String getSubtitle() {
        return getHeaderValue("SUBTITLE");
    }

    public String getArtist() {
        return getHeaderValue("ARTIST");
    }

    public String getGerne() {
        return getHeaderValue("GERNE");
    }

    public String getCredit() {
        return getHeaderValue("CREDIT");
    }

    public String getMusic() {
        return getHeaderValue("MUSIC");
    }

    public String getBackground() {
        return getHeaderValue("BACKGROUND");
    }

    public double getOffset() {
        return Double.parseDouble(
                hasHeader("OFFSET") ? getHeaderValue("OFFSET") : "0"
        );
    }

    public boolean hasHeader(String name) {
        return header.containsKey(name);
    }

    public String getHeaderValue(String name) {
        return header.getOrDefault(name, "");
    }

    public List<Chart> getCharts() {
        return charts;
    }

    @Override
    public String toString() {
        return "SimFile{" +
                "header=" + header +
                ", charts=" + charts +
                '}';
    }

    public static SimFile parse(Path path) throws
            IOException,
            IllegalArgumentException,
            IllegalStateException {
        return new SimFileParser(path).parse();
    }

}
