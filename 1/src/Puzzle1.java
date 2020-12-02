import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("Input File: ");
        Scanner input = new Scanner(new File(console.next()));
        console.close();

        List<Integer> list = new ArrayList<>(200);
        while (input.hasNextInt()) {
            list.add(input.nextInt());
        }
        input.close();

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
    }
}
