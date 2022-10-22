package gremlins;

public abstract class LiveEntity implements Sprite {
    protected Level currentLevel;
    protected int xPx;
    protected int yPx;
    protected int xOrigin;
    protected int yOrigin;

    protected int xTarget;
    protected int yTarget;
    protected int xVel = 0;
    protected int yVel = 0;

    protected char dir;

    /**
     * LiveEntity Class constructor.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param l Associated level object.
     * @return LiveEntity object.
     */
    public LiveEntity(int xPx, int yPx, Level l) {
        this.currentLevel = l;
        this.xPx = xPx;
        this.yPx = yPx;
        this.xOrigin = xPx;
        this.yOrigin = yPx;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }

    /**
     * Returns location index increment/decrement number based on LiveEntity directionality.
     * @return Integer location index difference.
     */
    abstract int getDirNum();

    /**
     * Calls LiveEntity to fire a projectile.
     */
    abstract void fire();

    /**
     * Returns whether Player is able to move to a location index.
     * @param idx Location index.
     * @return Boolean determining whether no Wall exists, or if it does, it is broken.
     */
    public boolean canMove(int idx) {
        return currentLevel.canWalk(idx);
    }
}
