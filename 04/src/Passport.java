import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Passport {
    private final Map<String, String> fields;
    private static final List<String> requiredFields = List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    private static final List<String> eyeColors = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    public Passport(String fieldString) {
        fields = new HashMap<>(8);
        Pattern p = Pattern.compile("(\\w+):(\\S+)");
        Matcher m = p.matcher(fieldString);
        while (m.find()) {
            fields.put(m.group(1), m.group(2));
        }
    }

    public boolean isValid(boolean enforceRestrictions) {
        return enforceRestrictions ? isValidRestrictions() : isValidNoRestrictions();
    }

    private boolean isValidNoRestrictions() {
        return fields.keySet().containsAll(requiredFields);
    }

    private boolean isValidRestrictions() {
        return isValidNoRestrictions() &&
                validbyr() &&
                validiyr() &&
                valideyr() &&
                validhgt() &&
                validhcl() &&
                validecl() &&
                validpid();
    }

    private boolean validbyr() {
        if (!CommonUtils.tryParseInt(fields.get("byr"))) {
            return false;
        }
        int year = Integer.parseInt(fields.get("byr"));
        return year >= 1920 && year <= 2002;
    }

    private boolean validiyr() {
        if (!CommonUtils.tryParseInt(fields.get("iyr"))) {
            return false;
        }
        int year = Integer.parseInt(fields.get("iyr"));
        return year >= 2010 && year <= 2020;
    }

    private boolean valideyr() {
        if (!CommonUtils.tryParseInt(fields.get("eyr"))) {
            return false;
        }
        int year = Integer.parseInt(fields.get("eyr"));
        return year >= 2020 && year <= 2030;
    }

    private boolean validhgt() {
        String heightString = fields.get("hgt");
        String heightValue = heightString.substring(0, heightString.length() - 2);
        if (!CommonUtils.tryParseInt(heightValue)) {
            return false;
        }
        int height = Integer.parseInt(heightValue);
        if (heightString.endsWith("cm")) {
            return height >= 150 && height <= 193;
        } else if (heightString.endsWith("in")) {
            return height >= 59 && height <= 76;
        } else {
            return false;
        }
    }

    private boolean validhcl() {
        Pattern p = Pattern.compile("#[0-9a-f]{6}");
        Matcher m = p.matcher(fields.get("hcl"));
        return m.matches();
    }

    private boolean validecl() {
        return eyeColors.contains(fields.get("ecl"));
    }

    private boolean validpid() {
        Pattern p = Pattern.compile("\\d{9}");
        Matcher m = p.matcher(fields.get("pid"));
        return m.matches();
    }

    public String toString() {
        return fields.toString();
    }
}
