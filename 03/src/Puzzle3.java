import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle3 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        System.out.println("===== Part 1 =====");
        List<String> list = new ArrayList<>(330);
        while (input.hasNextLine()) {
            list.add(input.nextLine());
        }
        River river = new River(list);
        System.out.println("Start row = 0, col = 0, slopeX = 3, slopeY = 1");
        System.out.printf("Hit %d trees%n", river.countTreesOnSlope(0, 0, 3, 1));

        System.out.println();
        System.out.println("===== Part 2 =====");
        int[][] slopes = {{1, 1}, {3, 1}, {5, 1}, {7,1}, {1,2}};
        int product = 1;
        for (int[] slope : slopes) {
            int trees = river.countTreesOnSlope(0, 0, slope[0], slope[1]);
            System.out.printf("Start row = 0, col = 0, slopeX = %d, slopeY = %d%n", slope[0], slope[1]);
            System.out.printf("Hit %d trees%n", trees);
            product *= trees;
        }
        System.out.printf("Product = %d%n", product);
    }
}
