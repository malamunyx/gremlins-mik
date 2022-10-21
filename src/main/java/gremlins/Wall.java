package gremlins;

import processing.core.PImage;

public class Wall extends Tile {
    private boolean breakable;
    private boolean broken = false;
    private int status = 0;
    private int timer = 16;


    /**
     * Constructor method for Wall class.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @return Instantiated Wall object.
     */
    public Wall(int xPx, int yPx, boolean breakable) {
        super(xPx, yPx);
        this.breakable = breakable;
    }

    /**
     * Calls the app to draw the map tile object every frame. Contains frame counting feature to update tile status.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable for image to be displayed.
     */
    @Override
    public void draw(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (broken && timer >= 0) {
            if (timer-- % 4 == 0) {
                ++status;
            }
        }
    }

    /**
     * Returns whether object can be breakable and interacted by Player.
     * @return Boolean variable representing Player interaction capability.
     */
    public boolean canBreak() {
        return this.breakable;
    }

    /**
     * Returns whether object has been interacted by Player.
     * @return Boolean variable representing whether Wall is broken.
     */
    public boolean isBroken() {
        return this.broken;
    }

    /**
     * Sets the wall broken status to true.
     */
    public void breakWall() {
        if (breakable)
            broken = true;
    }

    /**
     * Sets the wall broken status to false.
     */
    public void unbreakWall() {
        if (broken) {
            broken = false;
            status = 0;
            timer = 16;
        }
    }

    /**
     * Returns the Wall integer status. Utilised for PImage array traversal.
     * @return Integer representing Wall status.
     */
    public int getStatus() {
        return this.status;
    }
}
