import java.util.HashMap;
import java.util.Map;

public class DockingProgram {
    private String rawMask;
    private long inverseMask;
    private long positiveMask;
    private long xMask;
    private final Map<Long, Long> memory;

    public DockingProgram() {
        rawMask = "0";
        inverseMask = (long)Math.pow(2, 37) - 1;
        positiveMask = 0;
        xMask = 0;
        memory = new HashMap<>();
    }

    public void setMask(String mask, boolean useV2) {
        rawMask = mask;
        positiveMask = Long.parseLong(rawMask.replace('X', '0'), 2);
        if (useV2) {
            inverseMask = Long.parseLong(rawMask.replace('1', 'X').replace('0', '1').replace('X', '0'), 2);
            xMask = Long.parseLong(rawMask.replace('1', '0').replace('X', '1'), 2);
        } else {
            inverseMask = Long.parseLong(rawMask.replace('1', '0').replace('X', '1'), 2);
        }
    }

    public void writeMemory(long address, long value, boolean useV2) {
        if (useV2) {
            address &= inverseMask;
            address += positiveMask;
            writeFloating(address, xMask, value);
        } else {
            memory.put(address, (value & inverseMask) + positiveMask);
        }
    }

    private void writeFloating(long baseAddress, long floatingMask, long value) {
        if (floatingMask == 0) {
            memory.put(baseAddress, value);
        } else {
            long floatingBit = 1;
            while ((floatingBit & floatingMask) == 0 && floatingBit < Math.pow(2, 37)) {
                floatingBit *= 2;
            }
            writeFloating(baseAddress, floatingMask ^ floatingBit, value);
            writeFloating(baseAddress | floatingBit, floatingMask ^ floatingBit, value);
        }
    }

    public long getSum() {
        return memory.values().stream().reduce(Long::sum).orElse(0L);
    }
}
