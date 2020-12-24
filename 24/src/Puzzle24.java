import java.io.IOException;
import java.util.List;

public class Puzzle24 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();

        CommonUtils.printPartHeader(1);

        LobbyFloor lobbyFloor = new LobbyFloor();
        for (String line : input) {
            lobbyFloor.flipTile(line);
        }
        System.out.printf("Number of black tiles: %d%n%n", lobbyFloor.countBlackTiles());
    }
}
