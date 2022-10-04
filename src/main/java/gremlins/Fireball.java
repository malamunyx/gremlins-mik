package gremlins;

import processing.core.PImage;

public class Fireball extends Projectile implements Sprite{
    public Fireball(int xPx, int yPx, char dir, Level g) {
        super(xPx, yPx, dir, g);
    }

    /**
     * Calls the app to draw the Fireball and update its attributes every frame.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        // Automatically do wall collisions here.
        if (checkWallCollision()) {
            stop();
            this.neutralised = true;
            ((Wall) currentLevel.getTile(getIndex(getCentreX(), getCentreY()))).breakWall();
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

    /**
     * Returns integer location index based on Sprite pixel position.
     * @param xPx Horizontal pixel position.
     * @param yPx Vertical pixel position.
     * @return Integer index of ho location.
     */
    @Override
    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }

    /**
     * Return the horizontal pixel position of the Sprite centre.
     * @return Integer x plane pixel position.
     */
    @Override
    public int getCentreX() {
        return this.xPx + Sprite.xOffset;
    }

    /**
     * Return the vertical pixel position of the Sprite centre.
     * @return Integer y plane pixel position.
     */
    @Override
    public int getCentreY() {
        return this.yPx + Sprite.xOffset;
    }

    /**
     * Marks the Fireball projectile as neutralised, hence allowed for deletion in the next frame.
     */
    @Override
    public void reset() {
        this.neutralised = true;
    }
}

