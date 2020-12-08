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
        System.out.printf("%d possible exterior containers for a shiny gold bag%n%n", totalContainers(allBags, targetBag));

        CommonUtils.printPartHeader(2);
        System.out.printf("%d bags required inside a shiny gold bag%n", countTotalBags(targetBag) - 1);
    }

    private static Map<String, Bag> createBagMap(File inputFile) throws FileNotFoundException {
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

    private static void addBagContents(Map<String, Bag> allBags, File inputFile) throws FileNotFoundException {
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

    private static int totalContainers(Map<String, Bag> allBags, Bag bag) {
        return totalContainers(allBags, bag, new HashSet<>());
    }

    private static int totalContainers(Map<String, Bag> allBags, Bag containedBag, Set<Bag> containers) {
        for (String color : allBags.keySet()) {
            Bag bag = allBags.get(color);
            if (!containers.contains(bag) && bag.contains(containedBag) > 0) {
                containers.add(bag);
                totalContainers(allBags, bag, containers);
            }
        }
        return containers.size();
    }

    private static int countTotalBags(Bag exteriorBag) {
        return 1 + exteriorBag.getContents().keySet().stream().mapToInt(bag ->
            exteriorBag.contains(bag) * countTotalBags(bag)
        ).sum();
    }
}
