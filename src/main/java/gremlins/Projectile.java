package gremlins;

import processing.core.PImage;

public abstract class Projectile implements Sprite {
    protected Level currentLevel;
    protected static final int speed = 4;

    protected int xPx;
    protected int yPx;
    protected int xVel = 0;
    protected int yVel = 0;
    protected char dir;
    protected boolean neutralised = false;

    /**
     * Projectile Constructor.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param dir Direction character of the projectile [L, R, U, D].
     * @param l Associated level object.
     * @return Projectile object.
     */
    public Projectile(int xPx, int yPx, char dir, Level l) {
        this.currentLevel = l;
        this.xPx = xPx;
        this.yPx = yPx;
        this.dir = dir;
        setVelocity(dir);
    }


    /* SPRITE FUNCTIONS */

    /**
     * Calls app to draw projectile.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
    @Override
    public abstract void update(App a, PImage img);

    /**
     * Boolean checker that returns whether collision with Sprite object occurs.
     * @param s Objects implementing Sprite interface.
     * @return True whenever Sprites instances of specific objects have x and y distances less than sprite size (20 pixels).
     */
    @Override
    public abstract boolean spriteCollision(Sprite s);

    /**
     * Marks the Projectile as neutralised, hence allowed for deletion in the next frame.
     */
    @Override
    public void reset() {
        this.neutralised = true;
        stop();
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
     * Return the central horizontal pixel position of the Sprite centre.
     * @return Integer x plane pixel position.
     */
    @Override
    public int getCentreX() {
        return this.xPx + Sprite.Offset;
    }

    /**
     * Return the central vertical pixel position of the Sprite centre.
     * @return Integer y plane pixel position.
     */
    @Override
    public int getCentreY() {
        return this.yPx + Sprite.Offset;
    }


    /* PROJECTILE FUNCTIONS */

    /**
     * Returns the neutralised status of projectile.
     * @return True if projectile is neutralised, else returns false.
     */
    public boolean isNeutralised() {
        return this.neutralised;
    }

    /**
     * Halts Projectile movement, setting x and y velocities to 0.
     */
    public void stop() {
        xVel = 0;
        yVel = 0;
    }

    /**
     * Boolean checker that returns whether projectile collided with unbroken walls.
     * Checks existence of Wall object in its location index relative to the Sprite centre.
     * @return If wall exists, returns true if and only if Wall is broken, else return false.
     */
    public boolean checkWallCollision() {
        return !currentLevel.canWalk(getIndex(xPx + Offset, yPx + Offset));
    }

    /**
     * Sets the x or y velocity values based on the direction of projectile.
     * @param dir Only 'U', 'D', 'L', 'R' directions accepted; anything else is an illegal state.
     * @throws IllegalArgumentException Whenever any char parameter does not represent a direction.
     */
    private void setVelocity(char dir) throws IllegalArgumentException {
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
