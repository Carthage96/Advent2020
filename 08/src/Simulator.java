import java.util.ArrayList;
import java.util.List;

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

    public int runUntilDuplicate() {
        while (!instructions.get(ptr).hasExecuted) {
            instructions.get(ptr).execute();
        }
        return acc;
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
