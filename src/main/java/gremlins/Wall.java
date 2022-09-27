package gremlins;

import processing.core.PImage;

public class Wall extends Tile {
    private boolean breakable;
    private boolean broken;
    private int status;
    private int timer;



    public Wall(int xPx, int yPx, boolean breakable) {
        super(xPx, yPx);
        this.breakable = breakable;
        this.broken = false;
        this.status = 0;
        this.timer = 16;
    }

    @Override
    public void draw(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (broken && timer >= 0) {
            if (timer-- % 4 == 0) {
                ++status;
            }
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
            broken = true;
    }

    public int getStatus() {
        return this.status;
    }
}
