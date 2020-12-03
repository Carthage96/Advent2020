import java.util.List;

public class River {
    private final boolean[][] treeGrid;
    private final int nRows;
    private final int nCols;

    public River(List<String> input) {
        if (input.size() < 1) {
            throw new IllegalArgumentException("input must be non-empty list of Strings");
        }
        nCols = input.get(0).length();
        nRows = input.size();
        treeGrid = new boolean[nRows][nCols];
        for (int row = 0; row < nRows; row++) {
            String line = input.get(row);
            if (line.length() != nCols) {
                throw new IllegalArgumentException("All strings must be of equal length");
            }
            for (int col = 0; col < nCols; col++) {
                treeGrid[row][col] = line.charAt(col) == '#';
            }
        }
    }

    public int getnRows() {return nRows;}

    public int getnCols() {return nCols;}

    public boolean isTree(int row, int col) {
        if (row >= nRows) {
            throw new IllegalArgumentException("row is beyond the length of the river");
        }
        col = col % nCols;
        return treeGrid[row][col];
    }

    public int countTreesOnSlope(int startRow, int startCol, int slopeX, int slopeY) {
        int count = 0;
        int row = startRow;
        int col = startCol;
        while (row < nRows) {
            count += isTree(row, col) ? 1 : 0;
            row += slopeY;
            col += slopeX;
        }
        return count;
    }
}
