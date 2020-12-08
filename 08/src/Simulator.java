import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Simulator {
    private int acc;
    private int ptr;
    private final List<Instruction> instructions;

    public Simulator(List<String> code) {
        acc = 0;
        ptr = 0;
        instructions = new ArrayList<>(code.size());
        for (String line : code) {
            int value = Integer.parseInt(line.substring(4));
            switch (line.substring(0, 3)) {
                case "acc" -> instructions.add(new ACC(value));
                case "nop" -> instructions.add(new NOP(value));
                case "jmp" -> instructions.add(new JMP(value));
            }
        }
    }

    // Returns true if the program finished successfully (ready to execute first non-existent instruction),
    // false if it was about to enter an infinite loop
    public boolean run() {
        while (ptr < instructions.size() && !instructions.get(ptr).hasExecuted) {
            instructions.get(ptr).execute();
        }
        return ptr >= instructions.size();
    }

    public int getAcc() {
        return acc;
    }

    public int[] corruptCandidates() {
        return IntStream.range(0, instructions.size()).filter(x -> instructions.get(x).hasExecuted &&
                !(instructions.get(x) instanceof ACC)).toArray();
    }

    public void reset() {
        acc = 0;
        ptr = 0;
        for (Instruction instruction : instructions) {
            instruction.hasExecuted = false;
        }
    }

    public void swap(int index) {
        Instruction instruction = instructions.get(index);
        int value = instruction.value;
        if (instruction instanceof NOP) {
            instructions.set(index, new JMP(value));
        } else if (instruction instanceof JMP) {
            instructions.set(index, new NOP(value));
        }
    }

    private static abstract class Instruction {
        public final int value;
        public boolean hasExecuted;

        public Instruction(int value) {
            this.value = value;
        }

        public void execute() {
            executeInternal();
            hasExecuted = true;
        }

        public abstract void executeInternal();
    }

    private class ACC extends Instruction {
        public ACC(int value) {
            super(value);
        }

        @Override
        public void executeInternal() {
            acc += value;
            ptr++;
        }
    }

    private class NOP extends Instruction {
        public NOP(int value) {
            super(value);
        }

        @Override
        public void executeInternal() {
            ptr++;
        }
    }

    private class JMP extends Instruction {
        public JMP(int value) {
            super(value);
        }

        @Override
        public void executeInternal() {
            ptr += value;
        }
    }
}
