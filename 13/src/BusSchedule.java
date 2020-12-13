import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BusSchedule {
    private final List<Integer> buses;

    public BusSchedule(List<Integer> buses) {
        this.buses = new ArrayList<>(buses.size());
        this.buses.addAll(buses);
    }

    public int busWaitProduct(int minDepartTime) {
        List<Integer> waitTimes = buses.stream().map(bus -> waitTime(bus, minDepartTime)).collect(Collectors.toList());
        int minWait = Collections.min(waitTimes);
        int bus = buses.get(waitTimes.indexOf(minWait));
        return bus * minWait;
    }

    private static int waitTime(int schedule, int minDepartTime) {
        return (schedule - (minDepartTime % schedule));
    }
}
