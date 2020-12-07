import java.util.HashMap;
import java.util.Map;

public class Bag {
    private String color;
    private Map<String, Integer> contents;

    public Bag(String color, Map<String, Integer> contents) {
        this.color = color;
        this.contents = new HashMap<>(contents);
    }

    public Bag(String color) {
        this(color, new HashMap<>());
    }

    private int contains(String otherColor) {
        return contents.getOrDefault(otherColor, 0);
    }

    public int contains(Bag other) {
        return contains(other.color);
    }

    public void addContents(String color, int count) {
        contents.put(color, count);
    }

    public String getColor() {
        return color;
    }
}
