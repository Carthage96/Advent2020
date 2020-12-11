import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Puzzle11 {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        List<String> input = Files.readAllLines(new File(CommonUtils.prompt(console, "input file: ")).toPath());

        CommonUtils.printPartHeader(1);

        SeatingArea seatingArea = new SeatingArea(input, false);
        seatingArea.runUntilStable();
        System.out.printf("%d filled seats when stable%n%n", seatingArea.totalFull());

        CommonUtils.printPartHeader(2);

        seatingArea = new SeatingArea(input, true);
        seatingArea.runUntilStable();
        System.out.printf("%d filled seats when stable%n", seatingArea.totalFull());
        PrintStream output = new PrintStream(new File("output.txt"));
        seatingArea.write(output);
    }
}
