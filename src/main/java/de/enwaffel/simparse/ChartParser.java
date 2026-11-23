package de.enwaffel.simparse;

import java.util.ArrayList;
import java.util.List;

class ChartParser extends ParserBase {

    private final Timing timing;

    public ChartParser(List<BPMSegment> bpmSegments, List<BPMSegment> stopSegments, List<BPMSegment> delaySegments) {
        timing = new Timing(bpmSegments, stopSegments, delaySegments);
    }

    public Chart parse(String chartContent) {
        List<String> lines = chartContent.lines()
                .filter(s -> !s.isBlank())
                .map(s -> s.replace(":", ""))
                .map(String::trim)
                .toList();
        if (lines.size() < 5) throw new IllegalArgumentException("Missing chart metadata!");

        StepsType stepsType = StepsType.fromName(lines.get(0));
        if (stepsType == null) throw new IllegalArgumentException("Invalid steps-type: " + lines.get(0));

        int linesOffset = lines.size() < 6 ? 0 : 1;

        String desc = "";
        if (linesOffset < 1) {
            desc = lines.get(1);
        }
        String difficulty = lines.get(1 + linesOffset);
        int meter = Integer.parseInt(lines.get(2 + linesOffset));

        // collect measures
        List<List<String>> measures = new ArrayList<>();
        List<String> curMeasure = new ArrayList<>();
        for (String line : lines.subList(4 + linesOffset, lines.size())) {
            if (line.startsWith(",")) {
                measures.add(curMeasure);
                curMeasure = new ArrayList<>();
            } else {
                curMeasure.add(line);
            }
        }

        List<Note> notes = new ArrayList<>();
        double measureIndex = 0;
        for (List<String> measure : measures) {
            double rowIndex = 0;

            for (String row : measure) {
                double beatPos = 4 * (measureIndex + rowIndex / measure.size());

                for (int i = 0; i < row.length(); i++) {
                    char c = row.charAt(i);
                    if (c == '0') continue;

                    double seconds = timing.beatToSeconds(beatPos);
                    notes.add(new Note(seconds, i, NoteType.fromChar(row.charAt(i))));
                }

                rowIndex++;
            }

            measureIndex++;
        }

        return new Chart(
                stepsType,
                desc,
                difficulty,
                meter,
                notes
        );
    }

    private static class Timing {

        private final List<BPMSegment> segments;
        private final List<BPMSegment> stopSegments;
        private final List<BPMSegment> delaySegments;
        private final double[] bpmStart;

        Timing(List<BPMSegment> segments, List<BPMSegment> stops, List<BPMSegment> delays) {
            this.segments = segments;
            this.stopSegments = stops;
            this.delaySegments = delays;

            bpmStart = new double[segments.size()];
            for (int i = 1; i < segments.size(); i++) {
                bpmStart[i] = bpmStart[i - 1]
                        + (segments.get(i).beat() - segments.get(i - 1).beat())
                        * 60.0 / segments.get(i - 1).value();
            }
        }

        double beatToSeconds(double b) {
            int i = 0;
            while (i + 1 < segments.size() && segments.get(i + 1).beat() <= b) i++;

            double t = bpmStart[i] + (b - segments.get(i).beat()) * 60.0 / segments.get(i).value();

            for (BPMSegment s : stopSegments)  if (s.beat() <  b) t += s.value();
            for (BPMSegment s : delaySegments) if (s.beat() <= b) t += s.value();

            return t;
        }

    }

}
