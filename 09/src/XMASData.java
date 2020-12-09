import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class XMASData {
    private final List<Integer> data;
    private final int preambleSize;

    public XMASData(List<Integer> data, int preambleSize) {
        this.data = List.copyOf(data);
        this.preambleSize = preambleSize;
    }

    public int findFirstInvalid() {
        List<Integer> validSums = new LinkedList<>();
        // Prime list for preamble
        for (int i = 0; i < preambleSize; i++) {
            int value1 = data.get(i);
            for (int j = 0; j < i; j++) {
                int value2 = data.get(j);
                validSums.add(value1 != value2 ? value1 + value2 : null);
            }
            for (int j = 0; j < preambleSize - i; j++) {
                validSums.add(null);
            }
        }

        for (int i = preambleSize; i < data.size(); i++) {
            int newValue = data.get(i);
            if (!validSums.contains(newValue)) {
                return newValue;
            }
            validSums.subList(0, preambleSize).clear(); // Learned new thing!
            for (int j = i - preambleSize; j < i; j++) {
                validSums.add(newValue + data.get(j));
            }
        }
        throw new IllegalStateException("Searched data, but didn't find invalid entry!");
    }

    public int findWeakness(int invalid) {
        int start = 0;
        int end = 0;
        int sum = 0;
        while (sum != invalid) {
            if (sum < invalid) {
                sum += data.get(end);
                end++;
            } else {
                sum -= data.get(start);
                start++;
            }
        }
        int min = Collections.min(data.subList(start, end));
        int max = Collections.max(data.subList(start, end));
        return min + max;
    }
}
