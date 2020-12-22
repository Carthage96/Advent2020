import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CombatManager {
    private final Queue<Integer> deck1;
    private final Queue<Integer> deck2;

    public CombatManager(List<String> deck1, List<String> deck2) {
        this.deck1 = new LinkedList<>();
        this.deck2 = new LinkedList<>();
        for (String card : deck1) {
            this.deck1.add(Integer.parseInt(card));
        }
        for (String card : deck2) {
            this.deck2.add(Integer.parseInt(card));
        }
    }

    private boolean isGameOver() {
        return deck1.isEmpty() || deck2.isEmpty();
    }

    public int playToEnd() {
        int count = 0;
        while (!isGameOver()) {
            play1Round();
            count++;
        }
        return count;
    }

    private void play1Round() {
        if (isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }
        //noinspection ConstantConditions
        if (deck1.peek() > deck2.peek()) {
            deck1.add(deck1.remove());
            deck1.add(deck2.remove());
        } else {
            deck2.add(deck2.remove());
            deck2.add(deck1.remove());
        }
    }

    public long winnerScore() {
        Iterator<Integer> itr = (deck1.isEmpty() ? deck2 : deck1).iterator();
        long multiplier = deck1.isEmpty() ? deck2.size() : deck1.size();
        long total = 0;
        while (itr.hasNext()) {
            total += multiplier * itr.next();
            multiplier--;
        }
        return total;
    }
}
