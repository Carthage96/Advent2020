import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Bag {
    private String color;
    private Map<Bag, Integer> contents;

    public Bag(String color, Map<Bag, Integer> contents) {
        this.color = color;
        this.contents = new HashMap<>(contents);
    }

    public Bag(String color) {
        this(color, new HashMap<>());
    }

    public int contains(Bag other) {
        return contents.getOrDefault(other, 0);
    }

    public void addContents(Bag bag, int count) {
        contents.put(bag, count);
    }

    public String getColor() {
        return color;
    }

    public Map<Bag, Integer> getContents() {
        return Collections.unmodifiableMap(contents);
    }
}
