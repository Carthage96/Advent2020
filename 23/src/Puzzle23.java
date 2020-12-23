import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Puzzle23 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();
        List<Integer> order = Arrays.stream(input.next().split("")).map(Integer::parseInt).collect(Collectors.toList());

        CommonUtils.printPartHeader(1);

        CupCircle circle = new CupCircle(order);
        circle.doTurns(100);
        System.out.printf("Ending cup labels:%n%s%n%n", circle.labelString());
    }
}
