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

        long sum = input.stream().mapToLong(Puzzle18::compute).sum();
        System.out.printf("Sum = %d%n%n", sum);
    }

    public static long compute(String equation) {
        Pattern parenthesesPattern = Pattern.compile("\\(([^()]+)\\)");
        Matcher m = parenthesesPattern.matcher(equation);
        while (m.find()) {
            equation = m.replaceAll(Puzzle18::parensReplace);
            m.reset(equation);
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

    public static String parensReplace(MatchResult match) {
        return "" + compute(match.group(1));
    }
}
