import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Puzzle11 {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        List<String> input = Files.readAllLines(new File(CommonUtils.prompt(console, "input file: ")).toPath());

        SeatingArea seatingArea = new SeatingArea(input);

        CommonUtils.printPartHeader(1);

        seatingArea.runUntilStable();
        System.out.printf("%d filled seats when stable%n%n", seatingArea.totalFull());
    }
}
