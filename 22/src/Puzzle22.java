import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle22 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();
        List<Integer> deck1 = input.subList(1, input.indexOf("")).stream().map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> deck2 = input.subList(input.indexOf("") + 2, input.size()).stream().map(Integer::parseInt).collect(Collectors.toList());

        CommonUtils.printPartHeader(1);

        CombatManager combatManager = new CombatManager(deck1, deck2);
        int rounds = combatManager.playStandardCombat();
        System.out.printf("Game took %d rounds.%nWinner score: %d%n%n", rounds, combatManager.winnerScore());

        CommonUtils.printPartHeader(2);

        combatManager = new CombatManager(deck1, deck2);
        combatManager.playRecursiveCombat();
        System.out.printf("Winner score: %d%n", combatManager.winnerScore());
    }
}
