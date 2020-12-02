import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class Puzzle2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        int partNumber = getPartNumber(console);
        console.close();
        int validCount = 0;
        while(input.hasNextLine()) {
            String line = input.nextLine();
            Pattern p = Pattern.compile("(?<min>\\d+)-(?<max>\\d+) (?<letter>[a-z]): (?<password>\\w+)");
            Matcher m = p.matcher(line);
            if (!m.matches()) {
                System.out.println("Error: " + line);
                continue;
            }
            int min = Integer.parseInt(m.group("min"));
            int max = Integer.parseInt(m.group("max"));
            char letter = m.group("letter").charAt(0);
            String password = m.group("password");
            switch (partNumber) {
                case 1 -> {
                    if (isValidPart1(min, max, letter, password)) {
                        validCount++;
                    }
                }
                case 2 -> {
                    if (isValidPart2(min, max, letter, password)) {
                        validCount++;
                    }
                }
            }
        }
        System.out.printf("%d valid passwords found%n", validCount);
    }

    public static boolean isValidPart1(int min, int max, char letter, String password) {
        int count = CommonUtils.countCharInString(letter, password);
        return count >= min && count <= max;
    }

    public static boolean isValidPart2(int min, int max, char letter, String password) {
        return password.charAt(min - 1) == letter ^ password.charAt(max - 1) == letter;
    }

    public static int getPartNumber(Scanner console) {
        int part = 0;
        do {
            String input = CommonUtils.prompt(console, "Part number (1/2): ");
            try {
                part = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {}
            if (part < 1 || part > 2) {
                System.out.println("Not a valid part number. Try again.");
            }
        } while (part < 1 || part > 2);
        return part;
    }
}
