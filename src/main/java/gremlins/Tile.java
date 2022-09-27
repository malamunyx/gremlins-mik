package gremlins;

import processing.core.PImage;

public abstract class Tile {
    protected int xPx;
    protected int yPx;

    public static Wall brickWallFactory(int xPx, int yPx) {
        return new Wall(xPx, yPx, true);
    }

    public static Wall stoneWallFactory(int xPx, int yPx) {
        return new Wall(xPx, yPx, false);
    }

    public static Exit exitTileFactory(int xPx, int yPx) {
        return new Exit(xPx, yPx);
    }

    public Tile(int xPx, int yPx) {
        this.xPx = xPx;
        this.yPx = yPx;
    }

    public void draw(App a, PImage img) {
        a.image(img, xPx, yPx);
    }
}
