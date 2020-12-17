import java.util.*;

public class ConwayCubeSimulator {
    private final Map<Coordinate, Cube> space;
    private final Map<Coordinate, Cube> newCubes;
    private int step;

    public ConwayCubeSimulator(List<List<Boolean>> initialState) {
        space = new HashMap<>();
        for (int x = 0; x < initialState.size(); x++) {
            for (int y = 0; y < initialState.get(x).size(); y++) {
                Coordinate c = new Coordinate(x, y, 0);
                space.put(c, new Cube(c, initialState.get(x).get(y)));
                space.get(c).initialize();
            }
        }
        newCubes = new HashMap<>();
        step = 0;
    }

    public void advance1() {
        for (Cube cube : space.values()) {
            cube.computeNext();
        }
        Iterator<Cube> itr = newCubes.values().iterator();
        while (itr.hasNext()) {
            Cube cube = itr.next();
            cube.computeNext();
            if (cube.next) {
                cube.initialize();
            } else {
                itr.remove();
            }
        }
        space.putAll(newCubes);
        newCubes.clear();
        for (Cube cube : space.values()) {
            cube.advance();
        }
        step++;
    }

    public void advanceTo(int maxStep) {
        if (step > maxStep) {
            throw new IllegalStateException("Already passed step " + maxStep);
        }
        while (step < maxStep) {
            advance1();
        }
    }

    public long countActive() {
        return space.values().stream().filter(Cube::isActive).count();
    }

    @Override
    public String toString() {
        int minX = space.keySet().stream().mapToInt(c -> c.x).min().orElse(0);
        int maxX = space.keySet().stream().mapToInt(c -> c.x).max().orElse(0);
        int minY = space.keySet().stream().mapToInt(c -> c.y).min().orElse(0);
        int maxY = space.keySet().stream().mapToInt(c -> c.y).max().orElse(0);
        int minZ = space.keySet().stream().mapToInt(c -> c.z).min().orElse(0);
        int maxZ = space.keySet().stream().mapToInt(c -> c.z).max().orElse(0);
        StringBuilder builder = new StringBuilder();
        for (int z = minZ; z <= maxZ; z++) {
            builder.append("z=");
            builder.append(z);
            builder.append('\n');
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    Coordinate c = new Coordinate(x, y, z);
                    builder.append((space.containsKey(c) && space.get(c).isActive()) ? '#' : '.');
                }
                builder.append('\n');
            }
            builder.append('\n');
        }
        builder.append('\n');
        return builder.toString();
    }

    private class Cube {
        private final Coordinate coordinate;
        private boolean active;
        private boolean next;
        private boolean initialized;

        public Cube(Coordinate coordinate, boolean active) {
            this.coordinate = coordinate;
            this.active = active;
            next = active;
            initialized = false;
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        public boolean isActive() {
            return active;
        }

        public void computeNext() {
            int activeNeighbors = 0;
            for (int x = coordinate.x - 1; x <= coordinate.x + 1; x++) {
                for (int y = coordinate.y - 1; y <= coordinate.y + 1; y++) {
                    for (int z = coordinate.z - 1; z <= coordinate.z + 1; z++) {
                        Coordinate c = new Coordinate(x, y ,z);
                        if (c.equals(coordinate)) {
                            continue;
                        }
                        if (!space.containsKey(c) && !newCubes.containsKey(c) && initialized) {
                            newCubes.put(c, new Cube(c, false));
                        }
                        activeNeighbors += space.containsKey(c) && space.get(c).isActive() ? 1 : 0;
                    }
                }
            }
            if (active) {
                next = activeNeighbors == 2 || activeNeighbors == 3;
            } else {
                next = activeNeighbors == 3;
            }
        }

        public void initialize() {
            initialized = true;
        }

        public void advance() {
            active = next;
        }

        @Override
        public String toString() {
            return "Cube{" +
                    coordinate +
                    " " + (active ? "#" : ".") +
                    '}';
        }
    }

    private static class Coordinate {
        public final int x;
        public final int y;
        public final int z;

        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y &&
                    z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "{" + x + "," + y + "," + z + "}";
        }
    }
}
