import java.util.HashMap;
import java.util.Map;

public class MemoryGame {
    private int turn;
    private final Map<Integer, Integer> lastSaid;
    private int next;

    public MemoryGame(int[] start) {
        turn = 0;
        lastSaid = new HashMap<>();
        for (int i : start) {
            computeNext(i);
            lastSaid.put(i, turn);
            turn++;
        }
    }

    public int advanceTo(int lastTurn) {
        if (turn >= lastTurn) {
            throw new IllegalArgumentException("Turn " + lastTurn + " has already passed.");
        }
        int last = -1;
        while (turn < lastTurn) {
            last = advance();
        }
        return last;
    }

    public int advance() {
        int current = next;
        computeNext(current);
        lastSaid.put(current, turn);
        turn++;
        return current;
    }

    private void computeNext(int current) {
        next = lastSaid.containsKey(current) ? turn - lastSaid.get(current) : 0;
    }
}
