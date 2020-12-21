import java.util.*;
import java.util.stream.Collectors;

public class AllergenManager {
    private final Set<Food> allFoods;
    private final Set<Ingredient> allergenFreeIngredients;

    public AllergenManager(List<String> input) {
        allFoods = new HashSet<>();
        for (String foodLine : input) {
            String[] ingredients = foodLine.split("\\(")[0].strip().split(" ");
            String unsplitAllergens = foodLine.split("\\(")[1];
            unsplitAllergens = unsplitAllergens.substring("contains".length() + 1, unsplitAllergens.length() - 1);
            String[] allergens = unsplitAllergens.split(", ");
            allFoods.add(new Food(ingredients, allergens));
        }
        allergenFreeIngredients = new HashSet<>();
    }

    public void findAllergenFreeIngredients() {
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
    }

    public long countAllergenFreeIngredientOccurrences() {
        return countOccurrences(allergenFreeIngredients);
    }

    public long countOccurrences(Ingredient ingredient) {
        return allFoods.stream().filter(f -> f.containsIngredient(ingredient)).count();
    }

    public long countOccurrences(Collection<Ingredient> ingredients) {
        return ingredients.stream().mapToLong(this::countOccurrences).sum();
    }

    public void assignAllergens() {
        Set<Ingredient> ingredients = new HashSet<>(Ingredient.allIngredients());
        ingredients.removeAll(allergenFreeIngredients);
        Map<Allergen, Set<Ingredient>> allergenCandidates = new HashMap<>();
        for (Allergen allergen : Allergen.allAllergens()) {
            allergenCandidates.put(allergen, new HashSet<>(ingredients));
        }
        while (!allergenCandidates.isEmpty()) {
            Iterator<Allergen> allergenIterator = allergenCandidates.keySet().iterator();
            while (allergenIterator.hasNext()) {
                Allergen allergen = allergenIterator.next();
                // Set of all foods which are known to contain this allergen
                // If an ingredient is not present in all of them, it cannot contain this allergen
                Set<Food> listedFoods = foodsWithListedAllergen(allFoods, allergen);
                Iterator<Ingredient> itr = allergenCandidates.get(allergen).iterator();
                while (itr.hasNext()) {
                    Ingredient ingredient = itr.next();
                    if (!ingredients.contains(ingredient)) {
                        itr.remove();
                        continue;
                    }
                    if (listedFoods.stream().anyMatch(food -> !food.containsIngredient(ingredient))) {
                        itr.remove();
                    }
                }
                if (allergenCandidates.get(allergen).size() == 1) {
                    Ingredient ingredient = allergenCandidates.get(allergen).iterator().next();
                    ingredient.setAllergen(allergen);
                    allergenIterator.remove();
                    ingredients.remove(ingredient);
                }
            }
        }
    }

    private Set<Food> foodsWithListedAllergen(Collection<Food> foods, Allergen allergen) {
        return foods.stream().filter(f -> f.listsAllergen(allergen)).collect(Collectors.toSet());
    }

    public String canonicalDangerousIngredientsList() {
        Set<Ingredient> dangerousIngredients = new TreeSet<>();
        for (Ingredient ingredient : Ingredient.allIngredients()) {
            if (ingredient.getAllergen() != null) {
                dangerousIngredients.add(ingredient);
            }
        }
        Iterator<Ingredient> itr = dangerousIngredients.iterator();
        if (!itr.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(itr.next().getName());
        while (itr.hasNext()) {
            sb.append(',');
            sb.append(itr.next().getName());
        }
        return sb.toString();
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

    private static class Ingredient implements Comparable<Ingredient> {
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

        public void setAllergen(Allergen allergen) {
            this.allergen = allergen;
        }

        public Allergen getAllergen() {
            return allergen;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(Ingredient o) {
            if (this.allergen == null) {
                return o.allergen == null ? 0 : 1;
            } else if (o.allergen == null) {
                return -1;
            } else {
                return this.allergen.compareTo(o.allergen);
            }
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

    private static class Allergen implements Comparable<Allergen> {
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
        public int compareTo(Allergen o) {
            return this.name.compareTo(o.name);
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
