package gremlins;

public class Wall extends Tile{
    private boolean breakable;
    private boolean broken;
    private int status;

    public Wall(int x, int y, boolean breakable) {
        super(x, y);
        this.breakable = breakable;
        this.broken = false;
        this.status = 0;
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

    public void incrementStatus() {
        if (status < 5)
            ++this.status;
    }

    public int getStatus() {
        return this.status;
    }

    public int getIndex() {
        return x + y*36;
    }
}
