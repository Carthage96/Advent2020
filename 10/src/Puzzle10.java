import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Puzzle10 {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        List<Integer> adapters = Files.readAllLines(new File(CommonUtils.prompt(console, "input file: "))
                .toPath()).stream().map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
        console.close();

        AdapterChain adapterChain = new AdapterChain(adapters);

        CommonUtils.printPartHeader(1);

        Map<Integer, Integer> differences = adapterChain.getDifferences();
        System.out.printf("Product of 1-jolt and 3-jolt differences = %d%n%n", differences.get(1) * differences.get(3));

        CommonUtils.printPartHeader(2);
        System.out.printf("Total number of valid chains: %d%n", adapterChain.countValidChains());
    }
}
