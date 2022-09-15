package gremlins;

public class Wall extends Tile {
    // Determines whether stonewall or brickwall will be shown.
    private final boolean isBreakable;

    public Wall(int x, int y, boolean isBreakable) {
        super(x, y);
        this.isBreakable = isBreakable;
    }

    public boolean isBreakable() {
        return this.isBreakable;
    }
}
