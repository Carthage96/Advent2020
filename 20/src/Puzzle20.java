import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle20 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner input = new Scanner(new File(CommonUtils.prompt(console, "input file: ")));
        console.close();

        Map<Integer, List<String>> tileInput = new HashMap<>();
        Pattern idPattern = Pattern.compile("Tile (\\d+):");
        while (input.hasNextLine()) {
            String line = input.nextLine();
            Matcher m = idPattern.matcher(line);
            if (!m.matches()) {
                throw new IllegalArgumentException("Can't get tile id");
            }
            int id = Integer.parseInt(m.group(1));
            tileInput.put(id, new ArrayList<>());
            line = input.nextLine();
            while (!line.isEmpty()) {
                tileInput.get(id).add(line);
                line = input.nextLine();
            }
        }

        CommonUtils.printPartHeader(1);

        ImageReconstructor reconstructor = new ImageReconstructor(tileInput);
        reconstructor.resolveImage();
        System.out.printf("Corner product = %d%n%n", reconstructor.cornerProduct());

        CommonUtils.printPartHeader(2);
        Image restoredImage = reconstructor.toImage();
        int roughness = restoredImage.seaMonsterSearch();
        System.out.printf("Water roughness = %d%n", roughness);
    }
}
