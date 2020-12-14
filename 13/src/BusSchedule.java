import java.util.*;
import java.util.stream.Collectors;

public class BusSchedule {
    private final Map<Integer, Integer> buses;
    private final List<Integer> busesSorted;

    public BusSchedule(String[] buses) {
        this.buses = new HashMap<>(buses.length);
        this.busesSorted = new ArrayList<>(buses.length);
        for (int i = 0; i < buses.length; i++) {
            try {
                int bus = Integer.parseInt(buses[i]);
                this.buses.put(bus, i);
                this.busesSorted.add(bus);
            } catch (NumberFormatException ignored) {}
        }
        Collections.sort(busesSorted);
        Collections.reverse(busesSorted);
    }

    public int busWaitProduct(int minDepartTime) {
        List<Integer> waitTimes = busesSorted.stream().map(bus -> waitTime(bus, minDepartTime)).collect(Collectors.toList());
        int minWait = Collections.min(waitTimes);
        int bus = busesSorted.get(waitTimes.indexOf(minWait));
        return bus * minWait;
    }

    private static int waitTime(int schedule, int minDepartTime) {
        return (schedule - (minDepartTime % schedule));
    }

    public long magicTimestamp() {
        Iterator<Integer> itr = busesSorted.iterator();
        int x = itr.next();
        int y = itr.next();
        long firstSuccess = (y * firstNToMatch(x, y, buses.get(y) - buses.get(x), 0)) - buses.get(y);
        long lcm = lcm(x, y);
        while (itr.hasNext()) {
            x = itr.next();
            firstSuccess += lcm * firstNToMatch(x, lcm, -buses.get(x), firstSuccess);
            lcm = lcm(lcm, x);
        }
        return firstSuccess;
    }

    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    private static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    // offset is of y, relative to x
    // returns n such that
    // d + n*y = c + m*x
    // for some integer m
    private static long firstNToMatch(long x, long y, long c, long d) {
        long n = 1;
        while ((d + n * y - c) % x != 0) {
            n++;
        }
        return n;
    }
}
