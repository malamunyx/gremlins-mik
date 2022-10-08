package gremlins;

import processing.core.PImage;

public abstract class Tile {
    protected int xPx;
    protected int yPx;

    /**
     * Factory method that calls derived Wall Class constructor to produce a breakable brick wall.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @return Instantiated Wall object - brick wall.
     */
    public static Wall brickWallFactory(int xPx, int yPx) {
        return new Wall(xPx, yPx, true);
    }

    /**
     * Factory method that calls derived Wall Class constructor to produce an unbreakable brick wall.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @return Instantiated Wall object - stone wall.
     */
    public static Wall stoneWallFactory(int xPx, int yPx) {
        return new Wall(xPx, yPx, false);
    }

    /**
     * Factory method that calls derived Exit Class constructor to produce an Exit tile.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @return Instantiated Tile object - Exit tile.
     */
    public static Exit exitTileFactory(int xPx, int yPx) {
        return new Exit(xPx, yPx);
    }

    /**
     * Factory method that calls derived Powerup Class constructor to produce a Powerup tile.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @return Instantiated Tile object - Powerup tile.
     */
    public static Powerup powerupFactory(int xPx, int yPx) {
        return new Powerup(xPx, yPx);
    }

    public Tile(int xPx, int yPx) {
        this.xPx = xPx;
        this.yPx = yPx;
    }

    /**
     * Calls the app to draw the map tile object.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable for image to be displayed.
     */
    public void draw(App a, PImage img) {
        a.image(img, xPx, yPx);
    }
}
