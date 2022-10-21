package gremlins;

import processing.core.PImage;

public interface Sprite {
    /**
     * Sprite positionality is determined in the top left corner.
     * To determine centre, we add offset to the respective x and y pixel locations.
     */
    int Offset = 9;

    /**
     * Factory method that calls derived Player Class constructor to produce a Player object.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param l Associated level object.
     * @return Instantiated Player object.
     */
    static Player playerFactory(int xPx, int yPx, Level l) {
        return new Player(xPx, yPx, l);
    }

    /**
     * Factory method that calls derived Gremlin Class constructor to produce a Gremlin object.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param l Associated level object.
     * @return Instantiated Gremlin object.
     */
    static Gremlin gremlinFactory(int xPx, int yPx, Level l) {
        return new Gremlin(xPx, yPx, l);
    }

    /**
     * Factory method that calls derived Fireball Constructor to produce a Fireball object.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param dir Direction character of the projectile [L, R, U, D].
     * @param l Associated level object.
     * @return Instantiated Fireball object.
     */
    static Fireball fireballFactory(int xPx, int yPx, char dir, Level l) {
        return new Fireball(xPx, yPx, dir, l);
    }

    /**
     * Factory method that calls derived Iceball Constructor to produce a Iceball object.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param dir Direction character of the projectile [L, R, U, D].
     * @param l Associated level object.
     * @return Instantiated Iceball object.
     */
    static Iceball iceballFactory(int xPx, int yPx, char dir, Level l) {
        return new Iceball(xPx, yPx, dir, l);
    }

    /**
     * Factory method that calls derived SLime Constructor to produce a Slime object.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param dir Direction character of the projectile [L, R, U, D].
     * @param l Associated level object.
     * @return Instantiated Slime object.
     */
    static Slime slimeFactory(int xPx, int yPx, char dir, Level l) {
        return new Slime(xPx, yPx, dir, l);
    }

    /**
     * Calls the app to draw the Sprite whilst updating its attributes every frame.
     * @param a App class extending PApplet.
     * @param img Image for App class to draw.
     */
    void update(App a, PImage img);

    /**
     * Detection function mechanism for collision between Sprites.
     * @param s Object entity implementing Sprite interface.
     * @return True if and only if two sprites have x and y distance less than 20 pixels.
     */
    boolean spriteCollision(Sprite s);

    /**
     * Gets tile integer hash index to determine Sprite relative location with respect to the level tile map.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @return Hash index for comparison to level tile map.
     */
    int getIndex(int xPx, int yPx);

    /**
     * Determine the pixel position in x-axis of the sprite centre.
     * @return x-axis pixel position integer.
     */
    int getCentreX();

    /**
     * Determine the pixel position in y-axis of the sprite centre.
     * @return y-axis pixel position integer.
     */
    int getCentreY();

    /**
     * Calls for Sprite to reset.
     */
    void reset();
}
