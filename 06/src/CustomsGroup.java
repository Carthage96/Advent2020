import java.util.Set;
import java.util.TreeSet;

public class CustomsGroup {
    Set<Character> questions;

    public CustomsGroup() {
        questions = new TreeSet<>();
    }

    public void addQuestion(char q) {
        questions.add(q);
    }

    public void addQuestions(String q) {
        for (int i = 0; i < q.length(); i++) {
            addQuestion(q.charAt(i));
        }
    }

    public int yesCount() {
        return questions.size();
    }
}
