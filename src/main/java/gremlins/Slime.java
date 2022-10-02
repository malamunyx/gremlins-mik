package gremlins;

import processing.core.PImage;

public class Slime implements Projectile, Sprite{
    private Level currentLevel;
    private static final int speed = 4;

    private int xPx;
    private int yPx;
    private int xVel = 0;
    private int yVel = 0;
    private char dir;
    boolean neutralised = false;


    public Slime(int xPx, int yPx, char dir, Level g) {
        this.currentLevel = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.dir = dir;
        setVelocity(this.dir);
    }

    /**
     * Calls the app to draw the Slime and update its attributes every frame.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
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

    /**
     * Boolean checker that returns whether SLime collided with unbroken walls.
     * Checks existence of Wall object in its location index relative to the Sprite centre.
     * @return If wall exists, returns true if and only if Wall is broken, else return false.
     */
    @Override
    public boolean checkWallCollision() {
        return !currentLevel.canWalk(getIndex(xPx + xOffset, yPx + yOffset));
    }

    /**
     * Returns integer location index based on Sprite pixel position.
     * @param xPx Horizontal pixel position.
     * @param yPx Vertical pixel position.
     * @return Integer index of ho location.
     */
    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }

    /**
     * Returns the neutralised status of Slime projectile.
     * @return True if Fireball projectile is neutralised, else returns false.
     */
    @Override
    public boolean isNeutralised() {
        return this.neutralised;
    }

    /**
     * Halts Slime movement, setting x and y velocities to 0.
     */
    @Override
    public void stop() {
        xVel = 0;
        yVel = 0;
    }

    /**
     * Return the horizontal pixel position of the Sprite centre.
     * @return Integer x plane pixel position.
     */
    public int getCentreX() {
        return this.xPx + Sprite.xOffset;
    }

    /**
     * Return the vertical pixel position of the Sprite centre.
     * @return Integer y plane pixel position.
     */
    public int getCentreY() {
        return this.yPx + Sprite.xOffset;
    }

    /**
     * Marks the SLime projectile as neutralised, hence allowed for deletion in the next frame.
     */
    public void reset() {
        this.neutralised = true;
        stop();
    }

    /**
     * Sets the x or y velocity values based on the direction of Slime.
     * @param dir Only 'U', 'D', 'L', 'R' directions accepted; anything else is an illegal state.
     * @throws IllegalArgumentException Whenever any char parameter does not represent a direction.
     */
    private void setVelocity(char dir) throws IllegalArgumentException{
        switch (dir) {
            case 'L':
                xVel = -speed;
                break;
            case 'R':
                xVel = speed;
                break;
            case 'U':
                yVel = -speed;
                break;
            case 'D':
                yVel = speed;
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid char %c for projectile direction", dir));
        }
    }


}

