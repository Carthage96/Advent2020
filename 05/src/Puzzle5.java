import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Puzzle5 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        CommonUtils.printPartHeader(1);

        int min = 127 * 8 + 7;
        int max = 0;
        Set<Integer> seats = new TreeSet<>();
        while (input.hasNextLine()) {
            String boardingPass = input.nextLine();
            int row = binaryPartition(0, 127, boardingPass.substring(0, 7), 'F', 'B');
            int col = binaryPartition(0, 7, boardingPass.substring(7), 'L', 'R');
            int id = seatID(row, col);
            max = Math.max(max, id);
            min = Math.min(min, id);
            seats.add(id);
        }
        System.out.printf("Max SeatID = %d%n%n", max);

        CommonUtils.printPartHeader(2);
        // We prime this with the TreeSet that is populated during part 1
        for (int i = min + 1; i < max; i++) {
            if (!seats.contains(i)) {
                System.out.printf("Your seatID is: %d", i);
                break;
            }
        }
    }

    public static int binaryPartition(int lower, int upper, String partition, char lowerChar, char upperChar) {
        if (partition.isEmpty()) {
            if (lower == upper) {
                return lower;
            } else {
                throw new IllegalArgumentException("partition length was not sufficient to finish");
            }
        }
        char next = partition.charAt(0);
        if (next == lowerChar) {
            upper = lower + (upper - lower) / 2;
        } else if (next == upperChar) {
            lower = upper - (upper - lower) / 2;
        } else {
            throw new IllegalArgumentException("partition string contained illegal character");
        }
        return binaryPartition(lower, upper, partition.substring(1), lowerChar, upperChar);
    }

    public static int seatID(int row, int col) {
        return 8 * row + col;
    }
}
