import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LobbyFloor {
    private final Map<Point, Boolean> allTiles;

    public LobbyFloor() {
        allTiles = new HashMap<>();
        allTiles.put(new Point(0,0), false);
    }

    public void flipTile(String path) {
        flipTileInternal(path, 0, 0, 0);
    }

    private void flipTileInternal(String path, int index, int x, int y) {
        if (index == path.length()) {
            Point p = new Point(x, y);
            allTiles.put(p, !allTiles.getOrDefault(p, false));
        } else {
            switch (path.charAt(index)) {
                case 'e' -> flipTileInternal(path, index + 1, x + 1, y);
                case 'w' -> flipTileInternal(path, index + 1, x - 1, y);
                case 'n' -> {
                    switch (path.charAt(index + 1)) {
                        case 'e' -> flipTileInternal(path, index + 2, x + 1, y + 1);
                        case 'w' -> flipTileInternal(path, index + 2, x, y + 1);
                    }
                }
                case 's' -> {
                    switch (path.charAt(index + 1)) {
                        case 'e' -> flipTileInternal(path, index + 2, x, y - 1);
                        case 'w' -> flipTileInternal(path, index + 2, x - 1, y - 1);
                    }
                }
            }
        }
    }

    public long countBlackTiles() {
        return allTiles.values().stream().filter(x -> x).count();
    }

    private static class Point {
        public final int x;
        public final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
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
