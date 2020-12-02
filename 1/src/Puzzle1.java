import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "Input File: ")));
        console.close();

        List<Integer> list = new ArrayList<>(200);
        while (input.hasNextInt()) {
            list.add(input.nextInt());
        }
        input.close();

        System.out.println("===== Part 1 =====");
        boolean done = false;
        for (int i = 0; i < list.size() && !done; i++) {
            for (int j = i+1; j < list.size() && !done; j++) {
                if (list.get(i) + list.get(j) == 2020) {
                    System.out.println(list.get(i) + " + " + list.get(j) + " = 2020");
                    System.out.println(list.get(i) + " * " + list.get(j) + " = " + list.get(i) * list.get(j));
                    done = true;
                }
            }
        }

        System.out.println("===== Part 2 =====");
        done = false;
        for (int i = 0; i <list.size() && !done; i++) {
            int num1 = list.get(i);
            for (int j = i+1; j < list.size() && !done; j++) {
                int num2 = list.get(j);
                if (num1 + num2 > 2020) {
                    continue;
                }
                for (int k = j+1; k < list.size() && !done; k++) {
                    int num3 = list.get(k);
                    if (num1 + num2 + num3 == 2020) {
                        System.out.printf("%d + %d + %d = 2020%n", num1, num2, num3);
                        System.out.printf("%d * %d * %d = %d%n", num1, num2, num3, num1 * num2 * num3);
                        done = true;
                    }
                }
            }
        }
    }
}
