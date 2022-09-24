package gremlins;

public class Wall extends Tile{
    private boolean breakable;
    private boolean broken;

    public Wall(int x, int y, boolean breakable) {
        super(x, y);
        this.breakable = breakable;
        this.broken = false;
    }

    public boolean canBreak() {
        return this.breakable;
    }

    public boolean isBroken() {
        return this.broken;
    }

    public void breakWall() {
        if (breakable)
            this.broken = true;
    }
}
