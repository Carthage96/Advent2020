import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle17 {
    public static void main(String[] args) throws IOException {
        List<String> rawInput = CommonUtils.readInputToList();
        List<List<Boolean>> input = rawInput.stream()
                .map(s -> Arrays.stream(s.split("")).map(c -> c.charAt(0) == '#').collect(Collectors.toList()))
                .collect(Collectors.toList());

        CommonUtils.printPartHeader(1);

        ConwayCubeSimulator simulator = new ConwayCubeSimulator(input, false);
        simulator.advanceTo(6);
        System.out.printf("Total active: %d%n%n", simulator.countActive());

        CommonUtils.printPartHeader(2);

        simulator = new ConwayCubeSimulator(input, true);
        simulator.advanceTo(6);
        System.out.printf("Total active: %d%n%n", simulator.countActive());
    }
}
