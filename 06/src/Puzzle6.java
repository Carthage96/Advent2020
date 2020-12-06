import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle6 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        CommonUtils.printPartHeader(1);

        input.useDelimiter("\\n{2}");
        List<CustomsGroup> allGroups = new ArrayList<>(500);
        while (input.hasNext()) {
            String[] people = input.next().split("\\n");
            CustomsGroup group = new CustomsGroup();
            for (String person : people) {
                group.addQuestions(person);
            }
            allGroups.add(group);
        }
        int totalAny = allGroups.stream().map(CustomsGroup::yesCountAny).reduce(0, Integer::sum);
        System.out.printf("Total 'any' count: %d%n%n", totalAny);

        CommonUtils.printPartHeader(2);

        int totalEvery = allGroups.stream().map(CustomsGroup::yesCountEvery).reduce(0, Integer::sum);
        System.out.printf("Total 'every' count: %d%n%n", totalEvery);
    }
}
