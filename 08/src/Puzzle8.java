import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Puzzle8 {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        // Read input
        List<String> code =
                Files.readAllLines(new File(CommonUtils.prompt(console, "input file: ")).toPath(), Charset.defaultCharset());
        console.close();

        CommonUtils.printPartHeader(1);

        Simulator simulator = new Simulator(code);
        int lastAccValue = simulator.runUntilDuplicate();
        System.out.printf("acc value before repeated execution: %d%n", lastAccValue);
    }
}
