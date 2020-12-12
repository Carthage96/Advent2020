public class Ship {
    private int x;
    private int y;
    private int direction;
    private int waypointRelX;
    private int waypointRelY;

    public Ship() {
        x = 0;
        y = 0;
        direction = 0;
        waypointRelX = 10;
        waypointRelY = 1;
    }

    private void turn(int degrees) {
        direction = (direction + degrees + 360) % 360;
    }

    private void rotateWaypoint(int degrees) {
        if (degrees % 90 != 0) {
            throw new IllegalArgumentException("can only rotate waypoint in 90 degree increments");
        }
        degrees = (degrees + 360) % 360;
        for (int i = 0; i < degrees / 90; i++) {
            rotateWaypoint90();
        }
    }

    private void rotateWaypoint90() {
        int temp = waypointRelY;
        //noinspection SuspiciousNameCombination
        waypointRelY = waypointRelX;
        waypointRelX = -temp;
    }

    public void execute(String command, boolean isShip) {
        int value = Integer.parseInt(command.substring(1));
        char type = command.charAt(0);
        if (isShip) {
            executeShip(type, value);
        } else {
            executeWaypoint(type, value);
        }
    }

    public void executeShip(char type, int value) {
        switch (type) {
            case 'N' -> y += value;
            case 'S' -> y -= value;
            case 'E' -> x += value;
            case 'W' -> x -= value;
            case 'R' -> turn(-value);
            case 'L' -> turn(value);
            case 'F' -> {
                switch (direction) {
                    case 0 -> executeShip('E', value);
                    case 90 -> executeShip('N', value);
                    case 180 -> executeShip('W', value);
                    case 270 -> executeShip('S', value);
                }
            }
        }
    }

    private void executeWaypoint(char type, int value) {
        switch (type) {
            case 'N' -> waypointRelY += value;
            case 'S' -> waypointRelY -= value;
            case 'E' -> waypointRelX += value;
            case 'W' -> waypointRelX -= value;
            case 'R' -> rotateWaypoint(-value);
            case 'L' -> rotateWaypoint(value);
            case 'F' -> {
                if (value > 0) {
                    moveToWaypoint();
                    executeWaypoint(type, value - 1);
                }
            }
        }
    }

    private void moveToWaypoint() {
        x += waypointRelX;
        y += waypointRelY;
    }

    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}
