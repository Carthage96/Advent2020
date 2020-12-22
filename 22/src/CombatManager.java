import java.util.*;

public class CombatManager {
    private final Queue<Integer> deck1;
    private final Queue<Integer> deck2;
    private final Set<GameState> visitedStates;

    public CombatManager(List<Integer> deck1, List<Integer> deck2) {
        this.deck1 = new LinkedList<>(deck1);
        this.deck2 = new LinkedList<>(deck2);
        visitedStates = new HashSet<>();
    }

    private boolean isGameOver() {
        return deck1.isEmpty() || deck2.isEmpty();
    }

    public int playStandardCombat() {
        int count = 0;
        while (!isGameOver()) {
            play1RoundStandard();
            count++;
        }
        return count;
    }

    private void play1RoundStandard() {
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

    // Returns true if player 1 wins the game, false if player 2 wins the game
    public boolean playRecursiveCombat() {
        while (!isGameOver()) {
            GameState currentState = new GameState(deck1, deck2);
            if (visitedStates.contains(currentState)) {
                return true;
            }
            visitedStates.add(currentState);
            play1RoundRecursive();
        }
        return deck2.isEmpty();
    }

    private void play1RoundRecursive() {
        int card1 = deck1.remove();
        int card2 = deck2.remove();
        boolean player1Wins;
        if (deck1.size() >= card1 && deck2.size() >= card2) {
            CombatManager recursiveManager = new CombatManager(
                    ((LinkedList<Integer>)deck1).subList(0, card1),
                    ((LinkedList<Integer>)deck2).subList(0, card2));
            player1Wins = recursiveManager.playRecursiveCombat();
        } else {
            player1Wins = card1 > card2;
        }
        if (player1Wins) {
            deck1.add(card1);
            deck1.add(card2);
        } else {
            deck2.add(card2);
            deck2.add(card1);
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

    private static class GameState {
        private final Queue<Integer> deck1;
        private final Queue<Integer> deck2;

        public GameState(Queue<Integer> deck1, Queue<Integer> deck2) {
            this.deck1 = new LinkedList<>(deck1);
            this.deck2 = new LinkedList<>(deck2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameState gameState = (GameState) o;
            return deck1.equals(gameState.deck1) &&
                    deck2.equals(gameState.deck2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(deck1, deck2);
        }
    }
}
