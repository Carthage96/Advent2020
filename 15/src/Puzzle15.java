import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Puzzle15 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();
        int[] start = Arrays.stream(input.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
        input.close();

        CommonUtils.printPartHeader(1);
        MemoryGame game = new MemoryGame(start);
        int last = game.advanceTo(2020);
        System.out.printf("2020th number spoken: %d%n%n", last);

        CommonUtils.printPartHeader(2);
        last = game.advanceTo(30000000);
        System.out.printf("30000000th number spoken: %d%n%n", last);
    }
}
