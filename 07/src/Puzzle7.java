import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle7 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        CommonUtils.printPartHeader(1);

        Map<String, Bag> allBags = new HashMap<>();
        Pattern bagColorRegex = Pattern.compile("(?<color>.+) bags contain");
        Pattern bagContentsRegex = Pattern.compile("(?<count>\\d+) (?<color>[\\w ]+) bag");
        while (input.hasNextLine()) {
            String line = input.nextLine();
            Matcher m = bagColorRegex.matcher(line);
            //noinspection ResultOfMethodCallIgnored
            m.find();
            Bag bag = new Bag(m.group("color"));
            m = bagContentsRegex.matcher(line);
            while (m.find()) {
                bag.addContents(m.group("color"), Integer.parseInt(m.group("count")));
            }
            allBags.put(bag.getColor(), bag);
        }
        input.close();

        Bag targetBag = allBags.get("shiny gold");
        Set<Bag> allContainers = new HashSet<>();
        Set<Bag> latestContainers = Set.of(targetBag);
        do {
            Set<Bag> newContainers = new HashSet<>();
            for (Bag bag : allBags.values()) {
                if (!allContainers.contains(bag)) {
                    for (Bag container : latestContainers) {
                        if (bag.contains(container) > 0) {
                            newContainers.add(bag);
                            break;
                        }
                    }
                }
            }
            allContainers.addAll(newContainers);
            latestContainers = newContainers;
        } while (!latestContainers.isEmpty());

        System.out.printf("%d possible exterior containers for a shiny gold bag%n%n", allContainers.size());
    }
}
