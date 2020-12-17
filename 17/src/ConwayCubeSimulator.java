import java.util.*;

public class ConwayCubeSimulator {
    private final Map<Coordinate, Cube> space;
    private final Map<Coordinate, Cube> newCubes;
    private int step;

    public ConwayCubeSimulator(List<List<Boolean>> initialState, boolean is4D) {
        space = new HashMap<>();
        for (int x = 0; x < initialState.size(); x++) {
            for (int y = 0; y < initialState.get(x).size(); y++) {
                Coordinate c = is4D ? new Coordinate4D(x, y, 0, 0) : new Coordinate3D(x, y, 0);
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
            for (Coordinate c : coordinate.neighbors()) {
                if (!space.containsKey(c) && !newCubes.containsKey(c) && initialized) {
                    newCubes.put(c, new Cube(c, false));
                }
                activeNeighbors += space.containsKey(c) && space.get(c).isActive() ? 1 : 0;
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

    private interface Coordinate {
        Iterable<Coordinate> neighbors();
    }

    private static class Coordinate4D implements Coordinate {
        public final int x;
        public final int y;
        public final int z;
        public final int w;
        private final Neighbors n;

        public Coordinate4D(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            n = new Neighbors();
        }

        public Iterable<Coordinate> neighbors() {
            return n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate4D that = (Coordinate4D) o;
            return x == that.x &&
                    y == that.y &&
                    z == that.z &&
                    w == that.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, w);
        }

        @Override
        public String toString() {
            return "{" + x + "," + y + "," + z + "," + w + "}";
        }

        private class Neighbors implements Iterable<Coordinate> {
            @Override
            public Iterator<Coordinate> iterator() {
                return new NeighborIterator();
            }

            private class NeighborIterator implements Iterator<Coordinate> {
                private int xMod;
                private int yMod;
                private int zMod;
                private int wMod;

                public NeighborIterator() {
                    xMod = -1;
                    yMod = -1;
                    zMod = -1;
                    wMod = -1;
                }

                @Override
                public boolean hasNext() {
                    return xMod <= 1;
                }

                @Override
                public Coordinate next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Coordinate result = new Coordinate4D(x + xMod, y + yMod, z + zMod, w + wMod);
                    if (wMod < 1) {
                        wMod++;
                    } else {
                        wMod = -1;
                        if (zMod < 1) {
                            zMod++;
                        } else {
                            zMod = -1;
                            if (yMod < 1) {
                                yMod++;
                            } else {
                                yMod = -1;
                                xMod++;
                            }
                        }
                    }
                    return result.equals(Coordinate4D.this) ? next() : result;
                }
            }
        }
    }

    private static class Coordinate3D implements Coordinate {
        public final int x;
        public final int y;
        public final int z;
        private final Neighbors n;

        public Coordinate3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            n = new Neighbors();
        }

        public Iterable<Coordinate> neighbors() {
            return n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate3D that = (Coordinate3D) o;
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

        private class Neighbors implements Iterable<Coordinate> {
            @Override
            public Iterator<Coordinate> iterator() {
                return new NeighborIterator();
            }

            private class NeighborIterator implements Iterator<Coordinate> {
                private int xMod;
                private int yMod;
                private int zMod;

                public NeighborIterator() {
                    xMod = -1;
                    yMod = -1;
                    zMod = -1;
                }

                @Override
                public boolean hasNext() {
                    return xMod <= 1;
                }

                @Override
                public Coordinate next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Coordinate result = new Coordinate3D(x + xMod, y + yMod, z + zMod);
                    if (zMod < 1) {
                        zMod++;
                    } else {
                        zMod = -1;
                        if (yMod < 1) {
                            yMod++;
                        } else {
                            yMod = -1;
                            xMod++;
                        }
                    }
                    return result.equals(Coordinate3D.this) ? next() : result;
                }
            }
        }
    }
}
