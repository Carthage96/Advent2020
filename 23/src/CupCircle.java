import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CupCircle {
    private Cup currentCup;

    public CupCircle(List<Integer> initialOrder) {
        if (initialOrder.size() < 4) {
            throw new IllegalArgumentException("Cup circle must contain at least 4 cups");
        }
        currentCup = Cup.getInstance(initialOrder.get(0));
        Cup prev = currentCup;
        for (int i = 1; i < initialOrder.size(); i++) {
            Cup current = Cup.getInstance(initialOrder.get(i));
            prev.next = current;
            prev = current;
        }
        // close the loop
        prev.next = currentCup;
    }

    public void doTurns(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        for (int i = 0; i < n; i++) {
            do1Turn();
        }
    }

    private void do1Turn() {
        Cup firstRemoved = currentCup.next;
        currentCup.next = firstRemoved.next.next.next;
        int destIndex = clampIndex(currentCup.label - 1);
        while (destIndex == firstRemoved.label || destIndex == firstRemoved.next.label || destIndex == firstRemoved.next.next.label) {
            destIndex = clampIndex(destIndex - 1);
        }
        Cup destCup = Cup.getInstance(destIndex);
        firstRemoved.next.next.next = destCup.next;
        destCup.next = firstRemoved;
        currentCup = currentCup.next;
    }

    private int clampIndex(int index) {
        return index < 1 ? Cup.totalCups() - index : index;
    }

    public String labelString() {
        Cup cup1 = Cup.getInstance(1);
        Cup current = cup1.next;
        StringBuilder sb = new StringBuilder();
        while (current != cup1) {
            sb.append(current.label);
            current = current.next;
        }
        return sb.toString();
    }

    // Returns the product of the indices of the two cups following cup 1 in the current state of the circle
    // (This is the answer for part 2 of the puzzle)
    public long getCupProduct() {
        return (long)(Cup.getInstance(1).next.label) * (long)(Cup.getInstance(1).next.next.label);
    }

    private static class Cup {
        public final int label;
        public Cup next;
        private static final Map<Integer, Cup> allCups;

        static {
            allCups = new HashMap<>();
        }

        private Cup(int label) {
            this(label, null);
        }

        private Cup(int label, Cup next) {
            this.label = label;
            this.next = next;
        }

        public static Cup getInstance(int label) {
            if (!allCups.containsKey(label)) {
                allCups.put(label, new Cup(label));
            }
            return allCups.get(label);
        }

        public static int totalCups() {
            return allCups.size();
        }
    }
}
