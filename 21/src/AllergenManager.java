import java.util.*;

public class AllergenManager {
    private Set<Food> allFoods;

    public AllergenManager(List<String> input) {
        allFoods = new HashSet<>();
        for (String foodLine : input) {
            String[] ingredients = foodLine.split("\\(")[0].strip().split(" ");
            String unsplitAllergens = foodLine.split("\\(")[1];
            unsplitAllergens = unsplitAllergens.substring("contains".length() + 1, unsplitAllergens.length() - 1);
            String[] allergens = unsplitAllergens.split(", ");
            allFoods.add(new Food(ingredients, allergens));
        }
    }

    private Set<Ingredient> findAllergenFreeIngredients() {
        Set<Ingredient> allergenFreeIngredients = new HashSet<>();
        for (Ingredient ingredient : Ingredient.allIngredients()) {
            Set<Allergen> possibleAllergens = new HashSet<>(Allergen.allAllergens());
            for (Food food : allFoods) {
                if (!food.containsIngredient(ingredient)) {
                    possibleAllergens.removeAll(food.getKnownAllergens());
                    if (possibleAllergens.isEmpty()) {
                        break;
                    }
                }
            }
            if (possibleAllergens.isEmpty()) {
                allergenFreeIngredients.add(ingredient);
            }
        }
        return allergenFreeIngredients;
    }

    public long countAllergenFreeIngredientOccurrences() {
        return countOccurrences(findAllergenFreeIngredients());
    }

    public long countOccurrences(Ingredient ingredient) {
        return allFoods.stream().filter(f -> f.containsIngredient(ingredient)).count();
    }

    public long countOccurrences(Collection<Ingredient> ingredients) {
        return ingredients.stream().mapToLong(this::countOccurrences).sum();
    }

    private static class Food {
        private final Set<Ingredient> ingredients;
        private final Set<Allergen> knownAllergens;

        public Food(String[] ingredients, String[] knownAllergens) {
            this.ingredients = new HashSet<>();
            for (String ingredientName : ingredients) {
                this.ingredients.add(Ingredient.getInstance(ingredientName));
            }
            this.knownAllergens = new HashSet<>();
            for (String allergenName : knownAllergens) {
                this.knownAllergens.add(Allergen.getInstance(allergenName));
            }
        }

        public boolean containsIngredient(Ingredient ingredient) {
            return ingredients.contains(ingredient);
        }

        public Set<Allergen> getKnownAllergens() {
            return Collections.unmodifiableSet(knownAllergens);
        }

        // returns true if the food is stated to contain the allergen
        // Foods may have unlisted allergens
        public boolean listsAllergen(Allergen allergen) {
            return knownAllergens.contains(allergen);
        }

        @Override
        public String toString() {
            return "Food{" +
                    "ingredients=" + ingredients +
                    ", knownAllergens=" + knownAllergens +
                    '}';
        }
    }

    private static class Ingredient {
        private final String name;
        private Allergen allergen;
        private static final Map<String, Ingredient> allIngredients;

        static {
            allIngredients = new HashMap<>();
        }

        private Ingredient(String name) {
            this.name = name;
            allergen = null;
        }

        public static Ingredient getInstance(String name) {
            if (!allIngredients.containsKey(name)) {
                allIngredients.put(name, new Ingredient(name));
            }
            return allIngredients.get(name);
        }

        public static Collection<Ingredient> allIngredients() {
            return Collections.unmodifiableCollection(allIngredients.values());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Ingredient that = (Ingredient) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Ingredient{" +
                    name +
                    '}';
        }
    }

    private static class Allergen {
        private final String name;
        private static final Map<String, Allergen> allAllergens;

        static {
            allAllergens = new HashMap<>();
        }

        private Allergen(String name) {
            this.name = name;
        }

        public static Allergen getInstance(String name) {
            if (!allAllergens.containsKey(name)) {
                allAllergens.put(name, new Allergen(name));
            }
            return allAllergens.get(name);
        }

        public static Collection<Allergen> allAllergens() {
            return Collections.unmodifiableCollection(allAllergens.values());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Allergen allergen = (Allergen) o;
            return name.equals(allergen.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Allergen{" +
                    name +
                    '}';
        }
    }
}
