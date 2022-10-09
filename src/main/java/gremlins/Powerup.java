package gremlins;

import processing.core.PImage;

public class Powerup extends Tile {
    private int spawnDelay;
    private boolean canEffect;
    public Powerup(int xPx, int yPx) {
        super(xPx, yPx);
        spawnDelay = App.FPS * getRandomTimeInterval(5, 10);
        canEffect = false;
    }

    /**
     * Calls the app to draw the Powerup tile object.
     * Only displays podium if it has no active powerups.
     * Otherwise, it has the powerup sprite over the podium.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable for image to be displayed.
     */
    @Override
    public void draw(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (spawnDelay != 0) {
            --spawnDelay;
        } else {
            a.image(a.speedboots, xPx, yPx);
            canEffect = true;
        }
    }

    /**
     * Returns a random integer representative of time between an origin and a bound.
     * Interval notation: [Origin, bound)
     * @param origin Lowest integer the function can return.
     * @param bound Upper bound of returned integer.
     * @return Integer representative of time interval.
     */
    public int getRandomTimeInterval(int origin, int bound) {
        int t;
        do {
            t = Level.rg.nextInt(bound);
        } while (t < origin);

        return t;
    }

    /**
     * Returns whether the powerup can effect the player or not.
     * Tile collision with tile when the function returns false does nothing.
     * @return true whenever the powerup is active and can interact with player.
     */
    public boolean canEffectPlayer() {
        return canEffect;
    }

    /**
     * Reset the powerup to no longer effect player again, and start a delay timer.
     * getting random time (in seconds) until it is active again.
     * Time interval: [15, 30).
     */
    public void resetPowerup() {
        spawnDelay = getRandomTimeInterval(15, 30) * App.FPS;
        canEffect = false;
    }
}
