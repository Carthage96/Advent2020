import java.io.IOException;
import java.util.List;

public class Puzzle19 {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 2; i++) {
            CommonUtils.printPartHeader(i);
            List<String> input = CommonUtils.readInputToList();
            int split = input.indexOf("");
            MonsterMessageMatcher matcher = new MonsterMessageMatcher(input.subList(0, split));
            long matchCount = input.subList(split + 1, input.size()).stream().filter(s -> matcher.match(s, 0)).count();
            System.out.printf("Number of rule 0 matches: %d%n", matchCount);
            if (i == 1) {
                System.out.println();
            }
        }
    }
}
