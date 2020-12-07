import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle7 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        File inputFile = new File(CommonUtils.prompt(console, "input file: "));
        console.close();

        Map<String, Bag> allBags = createBagMap(inputFile);
        addBagContents(allBags, inputFile);

        CommonUtils.printPartHeader(1);

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

        CommonUtils.printPartHeader(2);
        System.out.printf("%d bags required inside a shiny gold bag%n", countTotalBags(allBags, targetBag) - 1);
    }

    public static Map<String, Bag> createBagMap(File inputFile) throws FileNotFoundException {
        Scanner input = new Scanner(inputFile);
        Map<String, Bag> allBags = new HashMap<>();
        Pattern bagColorRegex = Pattern.compile("(?:^| )(?<color>.+?) bag");
        while (input.hasNextLine()) {
            Matcher m = bagColorRegex.matcher(input.nextLine());
            while (m.find()) {
                if (!allBags.containsKey(m.group("color"))) {
                    allBags.put(m.group("color"), new Bag(m.group("color")));
                }
            }
        }
        input.close();
        return allBags;
    }

    public static void addBagContents(Map<String, Bag> allBags, File inputFile) throws FileNotFoundException {
        Scanner input = new Scanner(inputFile);
        Pattern exteriorBagRegex = Pattern.compile("^(?<color>.+?) bag");
        Pattern bagContentsRegex = Pattern.compile("(?<count>\\d+) (?<color>[\\w ]+) bag");
        while (input.hasNextLine()) {
            String line = input.nextLine();
            Matcher m = exteriorBagRegex.matcher(line);
            //noinspection ResultOfMethodCallIgnored
            m.find();
            Bag bag = allBags.get(m.group("color"));
            m = bagContentsRegex.matcher(line);
            while (m.find()) {
                bag.addContents(allBags.get(m.group("color")), Integer.parseInt(m.group("count")));
            }
            allBags.put(bag.getColor(), bag);
        }
        input.close();
    }

    public static int countTotalBags(Map<String, Bag> allBags, Bag exteriorBag) {
        return 1 + exteriorBag.getContents().keySet().stream().mapToInt(bag ->
            exteriorBag.contains(bag) * countTotalBags(allBags, bag)
        ).sum();
    }
}
