import java.util.HashMap;
import java.util.Map;

public class DockingProgram {
    private String rawMask;
    private long inverseMask;
    private long positiveMask;
    private final Map<Integer, Long> memory;

    public DockingProgram() {
        rawMask = "0";
        inverseMask = (long)Math.pow(2, 37) - 1;
        positiveMask = 0;
        memory = new HashMap<>();
    }

    public void setMask(String mask) {
        rawMask = mask;
        positiveMask = Long.parseLong(rawMask.replace('X', '0'), 2);
        inverseMask = Long.parseLong(rawMask.replace('1', '0').replace('X', '1'), 2);
    }

    public void writeMemory(int address, long value) {
        memory.put(address, (value & inverseMask) + positiveMask);
    }

    public long getSum() {
        return memory.values().stream().reduce(Long::sum).orElse(0L);
    }
}
