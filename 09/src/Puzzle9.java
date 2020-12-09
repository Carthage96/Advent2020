import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle9 {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        List<Integer> data = new ArrayList<>(1000);
        while (input.hasNextInt()) {
            data.add(input.nextInt());
        }
        input.close();

        XMASData xmas = new XMASData(data, 25);

        CommonUtils.printPartHeader(1);

        int invalid = xmas.findFirstInvalid();
        System.out.printf("First invalid entry: %d%n%n", invalid);

        CommonUtils.printPartHeader(2);

        int weakness = xmas.findWeakness(invalid);
        System.out.printf("Encryption Weakness: %d%n", weakness);
    }
}
