import java.util.Arrays;
import java.util.List;

public class SeatingArea {
    private final Seat[][] seatArray;
    private final int nRows;
    private final int nCols;

    public SeatingArea(List<String> input) {
        nRows = input.size();
        nCols = input.get(0).length();
        seatArray = new Seat[nRows][nCols];
        for (int row = 0; row < nRows; row++) {
            String rowString = input.get(row);
            for (int col = 0; col < nCols; col++) {
                switch (rowString.charAt(col)) {
                    case '.' -> seatArray[row][col] = new Seat(SeatState.FLOOR);
                    case 'L' -> seatArray[row][col] = new Seat(SeatState.EMPTY);
                    case '#' -> seatArray[row][col] = new Seat(SeatState.FULL);
                }
            }
        }
    }

    public int totalFull() {
        return (int)Arrays.stream(seatArray).flatMap(Arrays::stream).filter(x -> x.getCurrentState() == SeatState.FULL).count();
    }

    public boolean run1Step() {
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                seatArray[row][col].computeNext(buildNeighborInfo(row, col));
            }
        }
        boolean different = false;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                different |= seatArray[row][col].update();
            }
        }
        return different;
    }

    public void runUntilStable() {
        boolean different = true;
        while (different) {
            different = run1Step();
        }
    }

    private static class Seat {
        private SeatState currentState;
        private SeatState nextState;

        public Seat(SeatState state) {
            currentState = state;
            nextState = state;
        }

        public SeatState getCurrentState() {
            return currentState;
        }

        // Returns true if new state differs from old State
        public boolean update() {
            boolean different = currentState != nextState;
            currentState = nextState;
            return different;
        }

        public void computeNext(NeighborInfo neighbors) {
            switch (currentState) {
                case FLOOR -> nextState = SeatState.FLOOR;
                case EMPTY -> nextState = neighbors.countFull() == 0 ? SeatState.FULL : SeatState.EMPTY;
                case FULL -> nextState = neighbors.countFull() >= 4 ? SeatState.EMPTY : SeatState.FULL;
            }
        }
    }

    private NeighborInfo buildNeighborInfo(int centerRow, int centerCol) {
        SeatState[] states = new SeatState[8];
        for (int rowMod = -1; rowMod <= 1; rowMod++) {
            for (int colMod = -1; colMod <= 1; colMod++) {
                if (rowMod == 0 && colMod == 0) {
                    continue;
                }
                int row = centerRow + rowMod;
                int col = centerCol + colMod;
                boolean isValid = row >= 0 && row < seatArray.length && col >= 0 && col < seatArray[0].length;
                int index = (rowMod + 1) * 3 + (colMod + 1);
                // adjust to remove center element of grid
                index -= index > 4 ? 1 : 0;
                states[index] =
                        isValid ? seatArray[row][col].getCurrentState() : SeatState.UNKNOWN;
            }
        }
        return new NeighborInfo(states);
    }

    private static class NeighborInfo {
        // [0] [1] [2]
        // [3] [X] [4]
        // [5] [6] [7]
        private final SeatState[] neighbors;

        public NeighborInfo(SeatState[] neighbors) {
            this.neighbors = neighbors;
        }

        private int count(SeatState state) {
            return (int)Arrays.stream(neighbors).filter(x -> x == state).count();
        }

        public int countEmpty() {
            return count(SeatState.EMPTY);
        }

        public int countFull() {
            return count(SeatState.FULL);
        }

    }

    enum SeatState {
        FLOOR, EMPTY, FULL, UNKNOWN
    }
}


