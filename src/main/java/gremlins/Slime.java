package gremlins;

import processing.core.PImage;

public class Slime implements Projectile, Sprite{
    private Game currentGame;
    private static final int speed = 4;

    private int xPx;
    private int yPx;
    private int xVel = 0;
    private int yVel = 0;
    private char dir;
    boolean neutralised = false;


    public Slime(int xPx, int yPx, char dir, Game g) {
        this.currentGame = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.dir = dir;
        setVelocity(this.dir);
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

    @Override
    public boolean checkWallCollision() {
        return !currentGame.canWalk(getIndex(xPx + xOffset, yPx + yOffset));
    }

    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }
    public int getIndex() {
        return ((xPx + xOffset) / 20) + 36 * ((yPx + yOffset) / 20);
    }

    @Override
    public boolean isNeutralised() {
        return this.neutralised;
    }

    public void stop() {
        xVel = 0;
        yVel = 0;
    }

    public int getCentreX() {
        return this.xPx + Sprite.xOffset;
    }

    public int getCentreY() {
        return this.yPx + Sprite.xOffset;
    }

    public void reset() {
        this.neutralised = true;
        stop();
    }


    private void setVelocity(char dir) throws Error{
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
                throw new Error(String.format("Invalid char %c for projectile direction", dir));
        }
    }


}

