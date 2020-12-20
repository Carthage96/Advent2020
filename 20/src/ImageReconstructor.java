import java.util.*;

public class ImageReconstructor {
    private final List<Tile> unplacedTiles;
    private final Tile[][] placedTiles;
    private final int size;

    public ImageReconstructor(Map<Integer, List<String>> input) {
        unplacedTiles = new ArrayList<>(input.size());
        size = (int)Math.sqrt(input.size());
        placedTiles = new Tile[size][size];
        for (int id : input.keySet()) {
            unplacedTiles.add(new Tile(id, input.get(id)));
        }
    }

    public void resolveImage() {
        placeFirstCorner();
        // First we fill the first column
        for (int row = 1; row < size; row++) {
            Tile tile1 = placedTiles[row - 1][0];
            Iterator<Tile> itr = unplacedTiles.iterator();
            while (itr.hasNext()) {
                Tile tile2 = itr.next();
                boolean matched = false;
                for (int z = 0; z < 2; z++) {
                    for (int r = 0; r < 4; r++) {
                        if (Arrays.equals(tile1.bottomBorder(), tile2.topBorder())) {
                            placedTiles[row][0] = tile2;
                            itr.remove();
                            matched = true;
                            break;
                        }
                        tile2.rotate();
                    }
                    if (matched) {
                        break;
                    }
                    tile2.reflect();
                }
                if (matched) {
                    break;
                }
            }
        }
        // Now fill the rest
        for (int row = 0; row < size; row++) {
            for (int col = 1; col < size; col++) {
                Tile tile1 = placedTiles[row][col - 1];
                Iterator<Tile> itr = unplacedTiles.iterator();
                while (itr.hasNext()) {
                    Tile tile2 = itr.next();
                    boolean matched = false;
                    for (int z = 0; z < 2; z++) {
                        for (int r = 0; r < 4; r++) {
                            if (Arrays.equals(tile1.rightBorder(), tile2.leftBorder())) {
                                placedTiles[row][col] = tile2;
                                itr.remove();
                                matched = true;
                                break;
                            }
                            tile2.rotate();
                        }
                        if (matched) {
                            break;
                        }
                        tile2.reflect();
                    }
                    if (matched) {
                        break;
                    }
                }
            }
        }
        if (unplacedTiles.size() > 0) {
            throw new IllegalStateException("Failed to place all tiles!");
        }
    }

    private void placeFirstCorner() {
        // Find a tile which has two edges which don't match any edges from any other tiles
        Iterator<Tile> itr = unplacedTiles.iterator();
        while (itr.hasNext()) {
            Tile tile1 = itr.next();
            List<Tile> matches = new ArrayList<>();
            for (Tile tile2 : unplacedTiles) {
                if (matches.size() > 2) {
                    break;
                }
                if (tile1 == tile2) {
                    continue;
                }
                if (tilesShareEdge(tile1, tile2)) {
                    matches.add(tile2);
                }
            }
            if (matches.size() <= 2) {
                placedTiles[0][0] = tile1;
                itr.remove();

                // Now we must re-orient the tile so that its matching edges face inwards
                Tile tile2 = matches.get(0);
                while (!tileMatchesEdge(tile1.bottomBorder(), tile2)) {
                    tile1.rotate();
                }
                tile2 = matches.get(1);
                if (!tileMatchesEdge(tile1.rightBorder(), tile2)) {
                    tile1.reflect();
                }
                return;
            }
        }
    }

    private static boolean tileMatchesEdge(char[] edge, Tile tile) {
        for (int z = 0; z < 2; z++) {
            if (Arrays.equals(edge, tile.topBorder()) ||
            Arrays.equals(edge, tile.bottomBorder()) ||
            Arrays.equals(edge, tile.leftBorder()) ||
            Arrays.equals(edge, tile.rightBorder())) {
                return true;
            }
            tile.reflect();
            tile.rotate();
        }
        return false;
    }
    
    private static boolean tilesShareEdge(Tile t1, Tile t2) {
        return tileMatchesEdge(t1.topBorder(), t2) ||
                tileMatchesEdge(t1.bottomBorder(), t2) ||
                tileMatchesEdge(t1.leftBorder(), t2) ||
                tileMatchesEdge(t1.rightBorder(), t2);
    }

    public long cornerProduct() {
        return ((long)placedTiles[0][0].id) *
                placedTiles[0][size - 1].id *
                placedTiles[size - 1][0].id *
                placedTiles[size - 1][size - 1].id;
    }

    private static class Tile {
        private final int id;
        private char[][] image;

        public Tile(int id, List<String> data) {
            this.id = id;
            image = new char[data.size()][data.get(0).length()];
            for (int row = 0; row < data.size(); row++) {
                for (int col = 0; col < data.get(row).length(); col++) {
                    image[row][col] = data.get(row).charAt(col);
                }
            }
        }

        // Reflects the image across the vertical axis
        public void reflect() {
            for (char[] row : image) {
                reverseCharArray(row);
            }
        }

        // Rotates 90 degrees clockwise
        public void rotate() {
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

        public char[] topBorder() {
            return image[0];
        }

        public char[] bottomBorder() {
            return image[image.length - 1];
        }

        public char[] leftBorder() {
            char[] result = new char[image.length];
            for (int i = 0; i < image.length; i++) {
                result[i] = image[i][0];
            }
            return result;
        }

        public char[] rightBorder() {
            char[] result = new char[image.length];
            for (int i = 0; i < image.length; i++) {
                result[i] = image[i][image[i].length - 1];
            }
            return result;
        }

        public void print() {
            for (char[] row : image) {
                for (char c : row) {
                    System.out.print(c);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    // Reverses char array in-place
    public static void reverseCharArray(char[] input) {
        for (int i = 0; i < (input.length + 1) / 2; i++) {
            char temp = input[i];
            input[i] = input[input.length - 1 - i];
            input[input.length - 1 - i] = temp;
        }
    }
}
