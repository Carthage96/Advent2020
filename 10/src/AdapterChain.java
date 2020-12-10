import java.util.*;
import java.util.stream.IntStream;

public class AdapterChain {
    private final List<Integer> adapters;

    public AdapterChain(List<Integer> adapters) {
        this.adapters = adapters;
        this.adapters.add(Collections.max(this.adapters) + 3);
        this.adapters.add(0);
        Collections.sort(this.adapters);
    }

    public Map<Integer, Integer> getDifferences() {
        int[] differenceList = IntStream.range(1, adapters.size()).map(i -> adapters.get(i) - adapters.get(i-1)).toArray();
        Map<Integer, Integer> result = new HashMap<>();
        for (int diff : differenceList) {
            if (!result.containsKey(diff)) {
                result.put(diff, 0);
            }
            result.put(diff, result.get(diff) + 1);
        }
        return result;
    }

}
