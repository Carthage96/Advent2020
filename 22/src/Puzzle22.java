import java.io.IOException;
import java.util.List;

public class Puzzle22 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();
        List<String> deck1 = input.subList(1, input.indexOf(""));
        List<String> deck2 = input.subList(input.indexOf("") + 2, input.size());

        CombatManager combatManager = new CombatManager(deck1, deck2);

        CommonUtils.printPartHeader(1);

        int rounds = combatManager.playToEnd();
        System.out.printf("Game took %d rounds.%nWinner score: %d%n%n", rounds, combatManager.winnerScore());
    }
}
