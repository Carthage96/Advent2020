import java.util.HashMap;
import java.util.Map;

public class CustomsGroup {
    Map<Character, Integer> questions;
    int numPeople;

    public CustomsGroup() {
        questions = new HashMap<>();
        numPeople = 0;
    }

    private void addQuestion(char q) {
        if (!questions.containsKey(q)) {
            questions.put(q, 0);
        }
        questions.put(q, questions.get(q) + 1);
    }

    public void addQuestions(String s) {
        numPeople++;
        for (int i = 0; i < s.length(); i++) {
            addQuestion(s.charAt(i));
        }
    }

    public int yesCountAny() {
        return questions.size();
    }

    public int yesCountEvery() {
        return (int)questions.values().stream().filter(v -> v == numPeople).count();
    }
}
