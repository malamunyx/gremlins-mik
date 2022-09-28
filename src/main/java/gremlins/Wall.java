package gremlins;

import processing.core.PImage;

public class Wall extends Tile {
    private boolean breakable;
    private boolean broken = false;
    private int status = 0;
    private int timer = 16;



    public Wall(int xPx, int yPx, boolean breakable) {
        super(xPx, yPx);
        this.breakable = breakable;
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

    public void unbreakWall() {
        if (broken) {
            broken = false;
            status = 0;
            timer = 16;
        }
    }

    public int getStatus() {
        return this.status;
    }
}
