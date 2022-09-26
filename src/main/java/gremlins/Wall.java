package gremlins;

import processing.core.PImage;

public class Wall extends Tile{
    private boolean breakable;
    private boolean broken;
    private int status;
    private int timer;

    public Wall(int x, int y, boolean breakable) {
        super(x, y);
        this.breakable = breakable;
        this.broken = false;
        this.status = 0;
        this.timer = 16;
    }

    @Override
    public void draw(App a, PImage img, int x, int y) {
        a.image(img, x*20, y*20);
        if (broken && timer > 0) {
            if (timer-- % 4 == 0) {
                incrementStatus();
            }
//            --timer;
        }
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
