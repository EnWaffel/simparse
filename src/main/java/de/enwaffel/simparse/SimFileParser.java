package de.enwaffel.simparse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class SimFileParser extends ParserBase {

    private final Path path;

    private final List<String> chartContents = new ArrayList<>();
    private final List<BPMSegment> bpmSegments = new ArrayList<>();
    private final List<BPMSegment> stopSegments = new ArrayList<>();
    private final List<BPMSegment> delaySegments = new ArrayList<>();

    private final Map<String, String> header = new HashMap<>();
    private final List<Chart> charts = new ArrayList<>();

    SimFileParser(Path path) {
        this.path = path;
    }

    public SimFile parse() throws IOException, NumberFormatException {
        String content = Files.readString(path);
        if (content.isEmpty()) throw new IllegalArgumentException("Simfile is empty!");
        setupParser(content);

        parseHeader();
        if (chartContents.isEmpty()) throw new IllegalStateException("Simfile doesn't contain any charts!");
        if (!header.containsKey("BPMS")) throw new IllegalStateException("Simfile doesn't have a BPMS in its header!");

        parseBPMSegments("BPMS", bpmSegments);

        if (header.containsKey("STOPS")) {
            parseBPMSegments("STOPS", stopSegments);
        }
        if (header.containsKey("DELAYS")) {
            parseBPMSegments("DELAYS", delaySegments);
        }

        parseCharts();

        return new SimFile(
                header,
                charts
        );
    }

    private void parseCharts() throws NumberFormatException {
        for (String chartContent : chartContents) {
            ChartParser parser = new ChartParser(bpmSegments, stopSegments, delaySegments);
            charts.add(parser.parse(chartContent));
        }
    }

    private void parseHeader() {
        while (pos < contentLength()) {
            if (peek() == '/') {
                char c = next();
                while (c != '\n' && c != '\r') c = next();
                continue;
            }

            if (next() != '#') continue;

            String tagName = until(':').toUpperCase();
            skip(1); // skip ':'
            String tagContent = until(';');

            if (tagName.equalsIgnoreCase("NOTES")) {
                chartContents.add(tagContent);
            } else {
                header.put(tagName, tagContent);
            }
        }
    }

    private void parseBPMSegments(String headerName, List<BPMSegment> segments) {
        for (String entry : header.get(headerName).split(",")) {
            entry = entry.strip();
            if (entry.isEmpty()) continue;

            String[] parts = entry.split("=");
            if (parts.length != 2) continue;

            try {
                double beat = Double.parseDouble(parts[0].strip());
                double value = Double.parseDouble(parts[1].strip());
                segments.add(new BPMSegment(beat, value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Failed to parse value segments! (malformed number)");
            }
        }
    }

}
