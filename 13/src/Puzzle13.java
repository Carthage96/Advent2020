import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Puzzle13 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        int timeEstimate = Integer.parseInt(input.nextLine());
        String unparsedSchedule = input.nextLine();
        List<Integer> allBuses = Arrays.stream(unparsedSchedule.split(","))
                .filter(s -> !s.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        BusSchedule busSchedule = new BusSchedule(allBuses);

        CommonUtils.printPartHeader(1);

        System.out.printf("Earliest departure product: %d%n%n", busSchedule.busWaitProduct(timeEstimate));
    }
}
