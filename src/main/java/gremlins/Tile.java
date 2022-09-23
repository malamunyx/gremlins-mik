package gremlins;

import processing.core.PImage;

abstract class Tile {
    protected int x;
    protected int y;

    public Tile (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(App a, PImage img, int x, int y) {
        a.image(img, x*20, y*20);
    }

}
