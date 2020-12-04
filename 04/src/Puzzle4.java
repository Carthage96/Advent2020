import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        System.out.println("===== Part 1 =====");
        List<String> requiredFields = new ArrayList<>(7);
        requiredFields.add("byr");
        requiredFields.add("iyr");
        requiredFields.add("eyr");
        requiredFields.add("hgt");
        requiredFields.add("hcl");
        requiredFields.add("ecl");
        requiredFields.add("pid");

        List<Map<String, String>> passports = new ArrayList<>(300);
        for (String passportS : passportStrings) {
            Map<String, String> passport = new HashMap<>();
            Pattern p = Pattern.compile("(\\w+):(\\S+)");
            Matcher m = p.matcher(passportS);
            while (m.find()) {
                passport.put(m.group(1), m.group(2));
            }
            passports.add(passport);
        }

        int validCount = 0;
        for (Map<String, String> passport : passports) {
            boolean valid = true;
            for (String field : requiredFields) {
                if (!passport.containsKey(field)) {
                    valid = false;
                    break;
                }
            }
            validCount += valid ? 1 : 0;
        }
        System.out.println();
        System.out.printf("Found %d valid passports%n", validCount);
    }
}
