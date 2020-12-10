import java.util.*;
import java.util.stream.IntStream;

public class AdapterChain {
    private final List<Integer> adapters;

    public AdapterChain(List<Integer> adapters) {
        this.adapters = adapters;
        this.adapters.add(0);
        Collections.sort(this.adapters);
    }

    public Map<Integer, Integer> getDifferences() {
        int[] differenceList = IntStream.range(1, adapters.size()).map(i -> adapters.get(i) - adapters.get(i-1)).toArray();
        Map<Integer, Integer> result = new HashMap<>();
        for (int diff : differenceList) {
            result.put(diff, result.getOrDefault(diff, 0) + 1);
        }
        result.put(3, result.getOrDefault(3, 0) + 1);
        return result;
    }

    public long countValidChains() {
        long[] routeCount = new long[adapters.size()];
        routeCount[adapters.size() - 1] = 1;
        for (int i = adapters.size() - 2; i >= 0; i--) {
            for (int j = i + 1; j < adapters.size(); j++) {
                if (adapters.get(j) - adapters.get(i) <= 3) {
                    routeCount[i] += routeCount[j];
                } else {
                    break;
                }
            }
        }
        return routeCount[0];
    }
}
