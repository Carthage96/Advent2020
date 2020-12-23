import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Puzzle23 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();
        int[] order = Arrays.stream(input.next().split("")).mapToInt(Integer::parseInt).toArray();

        CommonUtils.printPartHeader(1);

        CupCircle circle = new CupCircle(order);
        circle.doTurns(100);
        System.out.printf("Ending cup labels:%n%s%n%n", circle.labelString());
    }
}
