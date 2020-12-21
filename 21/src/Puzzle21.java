import java.io.IOException;
import java.util.List;

public class Puzzle21 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();
        AllergenManager allergenManager = new AllergenManager(input);

        CommonUtils.printPartHeader(1);

        System.out.printf("Allergen free ingredients appearances: %d%n%n",
                allergenManager.countAllergenFreeIngredientOccurrences());
    }
}
