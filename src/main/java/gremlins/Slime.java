package gremlins;

import processing.core.PImage;

public class Slime implements Sprite {
    private final Game currentGame;
    private static final int speed = 4;
    int xPx;
    int yPx;
    int xVel;
    int yVel;
    char dir;
    boolean canRemove;


    public Slime (int xPx, int yPx, char dir, Game g) {
        this.currentGame = g;
        this.xPx = xPx;
        this.yPx = yPx;

        this.dir = dir;
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
        }
    }

    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        xPx += xVel;
        yPx += yVel;
    }

    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }
    public int getIndex() {
        return ((xPx+xOffset) / 20) + 36 * ((yPx+yOffset) / 20);
    }

    public boolean checkWallCollision() {
        return !currentGame.checkWall(getIndex(xPx + xOffset, yPx + yOffset));
    }

    public boolean intersects(Sprite s) {
        if (s instanceof Player || s instanceof Fireball) {
            int xDist = Math.abs(s.getCentreX() - this.getCentreX());
            int yDist = Math.abs(s.getCentreY() - this.getCentreY());
            return (xDist < 10 && yDist < 10);
        } else {
            return false;
        }
    }

    public void stop() {
        xVel = 0;
        yVel = 0;
    }

    public int getCentreX() {
        return this.xPx + xOffset;
    }

    public int getCentreY() {
        return this.yPx + yOffset;
    }

    public void reset(Game g) {
        stop();
        canRemove = true;
    }
}
