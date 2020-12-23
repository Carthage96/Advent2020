import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle23 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();
        List<Integer> order = new ArrayList<>(1000000);
        input.useDelimiter("");
        while (input.hasNextInt()) {
            order.add(input.nextInt());
        }

        CommonUtils.printPartHeader(1);

        CupCircle circle = new CupCircle(order);
        circle.doTurns(100);
        System.out.printf("Ending cup labels:%n%s%n%n", circle.labelString());

        CommonUtils.printPartHeader(2);

        while (order.size() < 1000000) {
            order.add(order.size() + 1);
        }
        circle = new CupCircle(order);
        circle.doTurns(10000000);
        System.out.printf("Cup product = %d%n", circle.getCupProduct());
    }
}
