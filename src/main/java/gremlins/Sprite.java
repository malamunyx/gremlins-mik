package gremlins;

import processing.core.PImage;

public interface Sprite {
    final static int xOffset = 9;
    final static int yOffset = 9;
    static Player playerFactory(int xPx, int yPx, Game g) {
        return new Player(xPx, yPx, g);
    }

    static Gremlin gremlinFactory(int xPx, int yPx, Game g) {
        return new Gremlin(xPx, yPx, g);
    }

    static Fireball fireballFactory(int xPx, int yPx, char dir, Game g) {
        return new Fireball(xPx, yPx, dir, g);
    }

    static Slime slimeFactory(int xPx, int yPx, char dir, Game g) {
        return new Slime(xPx, yPx, dir, g);
    }

    void update(App a, PImage img);

    //int getDirNum();

    int getIndex(int x, int y);

    boolean spriteCollision(Sprite s);


    int getCentreX();
    int getCentreY();

    void reset();
}
