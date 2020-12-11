import java.util.Arrays;
import java.util.List;

public class SeatingArea {
    private final Seat[][] seatArray;
    private final int nRows;
    private final int nCols;
    private final boolean isComplex;
    private static int[][] directions;

    static {
        staticInitializer();
    }

    private static void staticInitializer() {
        directions = new int[8][2];
        directions[0] = new int[]{-1, -1};
        directions[1] = new int[]{-1, 0};
        directions[2] = new int[]{-1, 1};
        directions[3] = new int[]{0, 1};
        directions[4] = new int[]{1, 1};
        directions[5] = new int[]{1, 0};
        directions[6] = new int[]{1, -1};
        directions[7] = new int[]{0, -1};
    }

    public SeatingArea(List<String> input, boolean isComplex) {
        this.isComplex = isComplex;
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

        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                setAwareSeats(row, col);
            }
        }
    }

    private void setAwareSeats(int row, int col) {
        // [0] [1] [2]
        // [7] [X] [3]
        // [6] [5] [4]
        if (isComplex) {
            setAwareSeatsComplex(row, col);
        } else {
            setAwareSeatsSimple(row, col);
        }
    }

    private void setAwareSeatsSimple(int row, int col) {
        Seat currentSeat = seatArray[row][col];
        for (int direction = 0; direction <= 7; direction++) {
            if (currentSeat.awareOf[direction] == null) {
                int otherRow = row + directions[direction][0];
                int otherCol = col + directions[direction][1];
                if (isValidLocation(otherRow, otherCol)) {
                    Seat otherSeat = seatArray[otherRow][otherCol];
                    currentSeat.awareOf[direction] = otherSeat;
                    otherSeat.awareOf[(direction + 4) % 8] = currentSeat;
                }
            }
        }
    }

    private void setAwareSeatsComplex(int row, int col) {

    }

    private boolean isValidLocation(int row, int col) {
        return row >= 0 && row < nRows && col >= 0 && col < nCols;
    }

    public int totalFull() {
        return (int)Arrays.stream(seatArray).flatMap(Arrays::stream).filter(x -> x.getCurrentState() == SeatState.FULL).count();
    }

    public boolean run1Step() {
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                seatArray[row][col].computeNext();
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
        private final Seat[] awareOf;

        public Seat(SeatState state) {
            currentState = state;
            nextState = state;
            awareOf = new Seat[8];
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

        private int neighborCount(SeatState state) {
            return (int)Arrays.stream(awareOf).filter(x -> x != null && x.currentState == state).count();
        }

        private int neighborsEmpty() {
            return neighborCount(SeatState.EMPTY);
        }

        private int neighborsFull() {
            return neighborCount(SeatState.FULL);
        }

        public void computeNext() {
            switch (currentState) {
                case FLOOR -> nextState = SeatState.FLOOR;
                case EMPTY -> nextState = neighborsFull() == 0 ? SeatState.FULL : SeatState.EMPTY;
                case FULL -> nextState = neighborsFull() >= 4 ? SeatState.EMPTY : SeatState.FULL;
            }
        }
    }

    enum SeatState {
        FLOOR, EMPTY, FULL
    }
}


