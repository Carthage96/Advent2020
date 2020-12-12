public class Ship {
    private int x;
    private int y;
    private int direction;

    public Ship() {
        x = 0;
        y = 0;
        direction = 0;
    }

    private void turn(int degrees) {
        direction = (direction + degrees + 360) % 360;
    }

    public void execute(String command) {
        int value = Integer.parseInt(command.substring(1));
        char type = command.charAt(0);
        switch (type) {
            case 'N' -> y += value;
            case 'S' -> y -= value;
            case 'E' -> x += value;
            case 'W' -> x -= value;
            case 'R' -> turn(-value);
            case 'L' -> turn(value);
            case 'F' -> {
                switch (direction) {
                    case 0 -> execute("E" + value);
                    case 90 -> execute("N" + value);
                    case 180 -> execute("W" + value);
                    case 270 -> execute("S" + value);
                }
            }
        }
    }

    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}
