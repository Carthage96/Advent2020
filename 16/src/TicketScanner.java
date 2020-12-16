import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketScanner {
    private final Map<String, Field> fields;

    public TicketScanner(List<String> rawFields) {
        fields = new HashMap<>();
        Pattern fieldRangePattern = Pattern.compile("(\\d+)-(\\d+)");
        for (String rawField : rawFields) {
            String name = rawField.substring(0, rawField.indexOf(':'));
            fields.put(name, new Field(name));
            Matcher m = fieldRangePattern.matcher(rawField);
            while (m.find()) {
                fields.get(name).addRange(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            }
        }
    }

    @Override
    public String toString() {
        return "TicketScanner{" +
                "fields=" + fields +
                '}';
    }

    public int errorRate(List<Integer> ticket) {
        int error = 0;
        for (int value : ticket) {
            if (fields.values().stream().noneMatch(field -> field.isValid(value))) {
                error += value;
            }
        }
        return error;
    }

    private static class Field {
        private final String name;
        private final Set<Range> ranges;

        public Field(String name) {
            this.name = name;
            this.ranges = new HashSet<>();
        }

        public void addRange(int min, int max) {
            ranges.add(new Range(min, max));
        }

        public boolean isValid(int value) {
            return ranges.stream().anyMatch(range -> range.contains(value));
        }
    }

    private static class Range {
        private final int min;
        private final int max;

        // Constructs a range with stated min and max (inclusive)
        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        // Returns whether a given int x is within the range
        public boolean contains(int x) {
            return x >= min && x <= max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range range = (Range) o;
            return min == range.min &&
                    max == range.max;
        }

        @Override
        public int hashCode() {
            return Objects.hash(min, max);
        }

        @Override
        public String toString() {
            return "Range{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }
}
