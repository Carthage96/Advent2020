import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class CommonUtils {
    public static String prompt(Scanner console, String prompt) {
        System.out.print(prompt);
        return console.nextLine();
    }

    public static List<String> readInputToList() throws IOException {
        Scanner console = new Scanner(System.in);
        return Files.readAllLines(new File(CommonUtils.prompt(console, "input file: ")).toPath());
    }

    // Returns the number of occurrences of a character c in a String s
    public static int countCharInString(char c, String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    // Returns the number of currences of a single character in a String s
    // Pre: c must be a String of length 1
    public static int countCharInString(String c, String s) {
        if (c.length() > 1) {
            throw new IllegalArgumentException("Only a single character to count allowed");
        }
        return countCharInString(c.charAt(0), s);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean tryParseInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Prints header for part to console
    public static void printPartHeader(int partNum) {
        System.out.printf("===== Part %d =====%n%n", partNum);
    }
}
