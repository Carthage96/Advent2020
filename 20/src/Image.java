import java.util.*;

public class Image {
    private char[][] image;
    private static final List<List<Integer>> relativeSMpositions;
    private final Set<Point> seaMonsterPoints;

    static {
        relativeSMpositions = new ArrayList<>();
        relativeSMpositions.add(List.of(0, 0));
        relativeSMpositions.add(List.of(1, 1));
        relativeSMpositions.add(List.of(1, 4));
        relativeSMpositions.add(List.of(0, 5));
        relativeSMpositions.add(List.of(0, 6));
        relativeSMpositions.add(List.of(1, 7));
        relativeSMpositions.add(List.of(1, 10));
        relativeSMpositions.add(List.of(0, 11));
        relativeSMpositions.add(List.of(0, 12));
        relativeSMpositions.add(List.of(1, 13));
        relativeSMpositions.add(List.of(1, 16));
        relativeSMpositions.add(List.of(0, 17));
        relativeSMpositions.add(List.of(0, 18));
        relativeSMpositions.add(List.of(0, 19));
        relativeSMpositions.add(List.of(-1, 18));
    }

    public Image(char[][] image) {
        this.image = image;
        seaMonsterPoints = new HashSet<>();
    }

    public int seaMonsterSearch() {
        int rotations = 0;
        while (seaMonsterPoints.isEmpty()) {
            for (int row = 0; row < image.length; row++) {
                for (int col = 0; col < image[0].length; col++) {
                    isSeaMonster(row, col);
                }
            }
            if (seaMonsterPoints.isEmpty()) {
                if (rotations < 3) {
                    rotations++;
                    rotate();
                } else {
                    rotations = 0;
                    reflect();
                }
            }
        }
        return totalHashtags() - seaMonsterPoints.size();
    }

    private int totalHashtags() {
        int count = 0;
        for (char[] row : image) {
            for (char c : row) {
                if (c == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    private void reflect() {
        for (char[] row : image) {
            ImageReconstructor.reverseCharArray(row);
        }
    }

    private void rotate() {
        int nRows = image.length;
        int nCols = image[0].length;
        char[][] result = new char[nCols][nRows];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                result[col][nRows - 1 - row] = image[row][col];
            }
        }
        image = result;
    }

    // Checks for sea monster, with leftmost # at the given position
    private void isSeaMonster(int row, int col) {
        if (row < 1 || row > image.length - 2 || col < 0 || col > image[0].length - 20) {
            return;
        }
        for (List<Integer> offsets : relativeSMpositions) {
            if (image[row + offsets.get(0)][col + offsets.get(1)] != '#') {
                return;
            }
        }
        // We found one - add the points
        for (List<Integer> offsets : relativeSMpositions) {
            seaMonsterPoints.add(new Point(row + offsets.get(0), col + offsets.get(1)));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : image) {
            sb.append(row);
            sb.append('\n');
        }
        return sb.toString();
    }

    private static class Point {
        private final int row;
        private final int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return row == point.row &&
                    col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}
