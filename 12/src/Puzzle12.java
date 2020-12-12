import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Puzzle12 {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        List<String> input = Files.readAllLines(new File(CommonUtils.prompt(console, "input file: ")).toPath());
        console.close();

        CommonUtils.printPartHeader(1);

        Ship ship = new Ship();
        for (String command : input) {
            ship.execute(command);
        }
        System.out.printf("Manhattan Distance: %d%n%n", ship.manhattanDistance());
    }
}
