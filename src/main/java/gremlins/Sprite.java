package gremlins;

import processing.core.PImage;

public interface Sprite {
    int Offset = 9;
    static Player playerFactory(int xPx, int yPx, Level g) {
        return new Player(xPx, yPx, g);
    }

    static Gremlin gremlinFactory(int xPx, int yPx, Level g) {
        return new Gremlin(xPx, yPx, g);
    }

    static Fireball fireballFactory(int xPx, int yPx, char dir, Level g) {
        return new Fireball(xPx, yPx, dir, g);
    }

    static Iceball iceballFactory(int xPx, int yPx, char dir, Level g) {
        return new Iceball(xPx, yPx, dir, g);
    }

    static Slime slimeFactory(int xPx, int yPx, char dir, Level g) {
        return new Slime(xPx, yPx, dir, g);
    }

    void update(App a, PImage img);
    boolean spriteCollision(Sprite s);

    int getIndex(int x, int y);

    int getCentreX();
    int getCentreY();

    void reset();
}
