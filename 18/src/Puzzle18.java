import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle18 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();

        CommonUtils.printPartHeader(1);

        long sum = input.stream().mapToLong(s -> compute(s, true)).sum();
        System.out.printf("Sum = %d%n%n", sum);

        CommonUtils.printPartHeader(2);
        sum = input.stream().mapToLong(s -> compute(s, false)).sum();
        System.out.printf("Sum = %d%n", sum);
    }

    public static long compute(String equation, boolean equalPrecedence) {
        Pattern parenthesesPattern = Pattern.compile("\\(([^()]+)\\)");
        Matcher m = parenthesesPattern.matcher(equation);
        while (m.find()) {
            equation = m.replaceAll(equalPrecedence ? Puzzle18::parensReplaceEqualPrecedence : Puzzle18::parensReplaceUnqualPrecedence);
            m.reset(equation);
        }
        if (!equalPrecedence) {
            Pattern additionPattern = Pattern.compile("(\\d+) \\+ (\\d+)");
            m = additionPattern.matcher(equation);
            while (m.find()) {
                equation = m.replaceAll(Puzzle18::additionReplace);
                m.reset(equation);
            }
        }
        Scanner eqScan = new Scanner(equation);
        long first = eqScan.nextInt();
        while (eqScan.hasNext()) {
            char operator = eqScan.next().charAt(0);
            long second = eqScan.nextInt();
            switch (operator) {
                case '+' -> first = first + second;
                case '*' -> first = first * second;
            }
        }
        return first;
    }

    public static String parensReplaceEqualPrecedence(MatchResult match) {
        return "" + compute(match.group(1), true);
    }

    public static String parensReplaceUnqualPrecedence(MatchResult match) {
        return "" + compute(match.group(1), false);
    }

    public static String additionReplace(MatchResult match) {
        return "" + (Long.parseLong(match.group(1)) + Long.parseLong(match.group(2)));
    }
}
