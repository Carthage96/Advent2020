import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketScanner {
    private final Map<String, Field> fields;
    private final List<Field> orderedFields;

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
        orderedFields = new ArrayList<>(fields.size());
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

    public void determineFieldOrder(List<List<Integer>> tickets) {
        for (int i = 0; i < fields.size(); i++) {
            orderedFields.add(null);
        }
        List<List<Field>> candidateFields = new ArrayList<>(fields.size());
        for (int i = 0; i < fields.size(); i++) {
            // Candidates start as all fields which have not already been placed
            candidateFields.add(fields.values().stream().filter(f -> !orderedFields.contains(f))
                    .collect(Collectors.toList()));
            // For each ticket
            for (List<Integer> ticket : tickets) {
                // For each field
                Iterator<Field> itr = candidateFields.get(i).iterator();
                while (itr.hasNext()) {
                    Field field = itr.next();
                    // Try to invalidate this field for this position
                    if (!field.isValid(ticket.get(i))) {
                        itr.remove();
                    }
                }
            }
            if (candidateFields.get(i).size() == 1) {
                orderedFields.set(i, candidateFields.get(i).get(0));
            }
        }
        while (orderedFields.contains(null)) {
            for (int i = 0; i < fields.size(); i++) {
                List<Field> candidates = candidateFields.get(i);
                candidates.removeIf(orderedFields::contains);
                if (candidates.size() == 1) {
                    orderedFields.set(i, candidates.get(0));
                }
            }
        }
    }

    public long departureProduct(List<Integer> ticket) {
        if (orderedFields.size() == 0) {
            throw new IllegalStateException("Must determine field order first");
        }
        return IntStream.range(0, ticket.size()).filter(x -> orderedFields.get(x).getName().contains("departure"))
                .mapToLong(ticket::get).reduce(1, (x, y) -> x * y);
    }

    private static class Field {
        private final String name;
        private final Set<Range> ranges;

        public Field(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Field name cannot be null");
            }
            this.name = name;
            this.ranges = new HashSet<>();
        }

        public String getName() {
            return name;
        }

        public void addRange(int min, int max) {
            ranges.add(new Range(min, max));
        }

        public boolean isValid(int value) {
            return ranges.stream().anyMatch(range -> range.contains(value));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Field field = (Field) o;
            return name.equals(field.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Field{" +
                    '\'' + name + '\'' +
                    '}';
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
