import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle4 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        input.useDelimiter("\\n{2,}");
        List<String> passportStrings = new ArrayList<>(500);
        while (input.hasNext()) {
            passportStrings.add(input.next().replace('\n', ' '));
        }

        List<Passport> passports = new ArrayList<>(300);
        for (String passportString : passportStrings) {
            passports.add(new Passport(passportString));
        }

        System.out.println("===== Part 1 =====\n");

        int validCount1 = (int)passports.stream().filter(passport -> passport.isValid(false)).count();

        System.out.printf("Found %d valid passports%n%n", validCount1);

        System.out.println("===== Part 2 =====\n");

        int validCount2 = (int)passports.stream().filter(passport -> passport.isValid(true)).count();
        System.out.printf("Found %d valid passports%n", validCount2);
    }
}
