import java.util.*;

public class LobbyFloor {
    private final Map<Point, Tile> allTiles;

    public LobbyFloor() {
        allTiles = new HashMap<>();
        //allTiles.put(new Point(0,0), new Tile(new Point(0, 0)));
    }

    public void flipTile(String path) {
        flipTileInternal(path, 0, 0, 0);
    }

    private void flipTileInternal(String path, int index, int x, int y) {
        if (index == path.length()) {
            getTile(new Point(x, y)).flip();
        } else {
            switch (path.charAt(index)) {
                case 'e' -> flipTileInternal(path, index + 1, x + 1, y);
                case 'w' -> flipTileInternal(path, index + 1, x - 1, y);
                case 'n' -> {
                    switch (path.charAt(index + 1)) {
                        case 'e' -> flipTileInternal(path, index + 2, x, y + 1);
                        case 'w' -> flipTileInternal(path, index + 2, x - 1, y + 1);
                    }
                }
                case 's' -> {
                    switch (path.charAt(index + 1)) {
                        case 'e' -> flipTileInternal(path, index + 2, x + 1, y - 1);
                        case 'w' -> flipTileInternal(path, index + 2, x, y - 1);
                    }
                }
            }
        }
    }

    public long countBlackTiles() {
        return allTiles.values().stream().filter(x -> x.isBlack).count();
    }

    public void advanceDays(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        purgeWhiteTiles();
        for (int i = 0; i < n; i++) {
            advance1Day();
        }
    }

    private void advance1Day() {
        Set<Point> newPoints = new HashSet<>();
        for (Tile tile : allTiles.values()) {
            newPoints.addAll(tile.computeNext());
        }
        for (Point point : newPoints) {
            // getTile will add these new tiles to allTiles
            getTile(point).computeNext();
        }
        for (Tile tile : allTiles.values()) {
            tile.advance();
        }
        // To prevent the number of tiles we look at each day from ballooning unnecessarily
        purgeWhiteTiles();
    }

    private void purgeWhiteTiles() {
        allTiles.values().removeIf(tile -> !tile.isBlack);
    }

    // returns tile that exists at this point - creates if object does not yet exist
    private Tile getTile(Point p) {
        if (!allTiles.containsKey(p)) {
            allTiles.put(p, new Tile(p));
        }
        return allTiles.get(p);
    }

    private boolean isTileBlack(Point p) {
        return allTiles.containsKey(p) && allTiles.get(p).isBlack;
    }

    private class Tile {
        public boolean isBlack;
        private boolean next;
        private final Point point;

        public Tile(Point point) {
            this(point, false);
        }

        public Tile(Point point, boolean isBlack) {
            this.point = point;
            this.isBlack = isBlack;
            this.next = this.isBlack;
        }

        public void flip() {
            isBlack = !isBlack;
        }

        public Set<Point> computeNext() {
            Set<Point> newTouchedPoints = new HashSet<>();
            int count = 0;
            for (int y = point.y - 1; y <= point.y + 1; y++) {
                for (int x = point.x - 1; x <= point.x + 1; x++) {
                    Point newPoint = new Point(x, y);
                    if (point.isNeighbor(newPoint)) {
                        count += isTileBlack(newPoint) ? 1 : 0;
                        if (!allTiles.containsKey(newPoint)) {
                            newTouchedPoints.add(newPoint);
                        }
                    }
                }
            }
            if (isBlack) {
                next = count == 1 || count == 2;
            } else {
                next = count == 2;
            }
            return newTouchedPoints;
        }

        public void advance() {
            isBlack = next;
        }
    }

    private static class Point {
        public final int x;
        public final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isNeighbor(Point other) {
            if (this.equals(other)) {
                return false;
            }
            int totalDistance = other.y - this.y + other.x - this.x;
            return Math.abs(this.y - other.y) <= 1 &&
                    totalDistance >= ((other.y > this.y) ? 0 : -1) &&
                    totalDistance <= ((other.y < this.y) ? 0 : 1);
        }

        @Override
        public String toString() {
            return "Point{" +
                    x + ',' +
                    y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
