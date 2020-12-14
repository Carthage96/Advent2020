import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle14 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();

        CommonUtils.printPartHeader(1);

        DockingProgram program = new DockingProgram();
        executeInput(program, input, false);
        System.out.printf("Sum of values in memory = %d%n%n", program.getSum());

        CommonUtils.printPartHeader(2);

        program = new DockingProgram();
        executeInput(program, input, true);
        System.out.printf("Sum of values in memory = %d%n%n", program.getSum());
    }

    public static void executeInput(DockingProgram program, List<String> input, boolean useV2) {
        Pattern memPattern = Pattern.compile("mem\\[(?<address>\\d+)] = (?<value>\\d+)");
        for (String line : input) {
            if (line.startsWith("mask")) {
                program.setMask(line.split(" ")[2], useV2);
            } else {
                Matcher m = memPattern.matcher(line);
                if (!m.matches()) {
                    throw new IllegalArgumentException("Invalid line in input: " + line);
                }
                program.writeMemory(Integer.parseInt(m.group("address")), Long.parseLong(m.group("value")), useV2);
            }
        }
    }
}
