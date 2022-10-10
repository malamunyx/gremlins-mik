package gremlins;

import processing.core.PImage;

public class Iceball extends Projectile {
    public Iceball (int xPx, int yPx, char dir, Level g) {
        super(xPx, yPx, dir, g);
    }

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
     * Boolean checker that returns whether collision with Gremlin or Slime object occurs.
     * @param s Objects implementing Sprite interface.
     * @return True whenever Sprites instances of specific objects have x and y distances less than sprite size (20 pixels).
     */
    @Override
    public boolean spriteCollision(Sprite s) {
        if (s instanceof Gremlin || s instanceof Slime) {
            int xDist = Math.abs(s.getCentreX() - this.getCentreX());
            int yDist = Math.abs(s.getCentreY() - this.getCentreY());
            return (xDist < App.SPRITESIZE && yDist < App.SPRITESIZE);
        } else {
            return false;
        }
    }
}
