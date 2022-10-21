package gremlins;

import processing.core.PImage;

public class Slime extends Projectile {
    /**
     * Slime Constructor.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param dir Direction character of the projectile [L, R, U, D].
     * @param l Associated level object.
     * @return Slime object.
     */
    public Slime(int xPx, int yPx, char dir, Level l) {
        super(xPx, yPx, dir, l);
    }

    /**
     * Calls the app to draw the Slime and update its attributes every frame.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        // Automatically do wall collisions here.
        if (checkWallCollision()) {
            stop();
            this.neutralised = true;
        }

        xPx += xVel;
        yPx += yVel;
    }

    /**
     * Boolean checker that returns whether collision with Player or Fireball object occurs.
     * @param s Objects implementing Sprite interface.
     * @return True whenever Sprites instances of specific objects have x and y distances less than sprite size (20 pixels).
     */
    @Override
    public boolean spriteCollision(Sprite s) {
        if (s instanceof Player || s instanceof Fireball) {
            int xDist = Math.abs(s.getCentreX() - this.getCentreX());
            int yDist = Math.abs(s.getCentreY() - this.getCentreY());
            return (xDist < App.SPRITESIZE && yDist < App.SPRITESIZE);
        } else {
            return false;
        }
    }
}

