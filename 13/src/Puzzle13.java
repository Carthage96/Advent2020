import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Puzzle13 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        int timeEstimate = Integer.parseInt(input.nextLine());
        String unparsedSchedule = input.nextLine();
        String[] allBuses = unparsedSchedule.split(",");
        BusSchedule busSchedule = new BusSchedule(allBuses);

        CommonUtils.printPartHeader(1);

        System.out.printf("Earliest departure product: %d%n%n", busSchedule.busWaitProduct(timeEstimate));

        CommonUtils.printPartHeader(2);

        System.out.printf("Magic timestamp: %d%n", busSchedule.magicTimestamp());
    }
}
