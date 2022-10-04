package gremlins;

public abstract class LiveEntity {
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

    public LiveEntity(int xPx, int yPx, Level g) {
        this.currentLevel = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.xOrigin = xPx;
        this.yOrigin = yPx;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }

    abstract int getDirNum();

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
