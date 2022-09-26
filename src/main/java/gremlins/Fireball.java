package gremlins;

import processing.core.PImage;

import java.util.Iterator;

public class Fireball implements Sprite {
    private final Game currentGame;
    private static final int speed = 4;
    int xPx;
    int yPx;
    int xVel;
    int yVel;
    int imgDir;
    boolean canRemove;


    public Fireball(int xPx, int yPx, int imgDir, Game g) {
        this.currentGame = g;
        this.xPx = xPx;
        this.yPx = yPx;

        this.imgDir = imgDir;
        switch (imgDir) {
            case 0:
                xVel = -speed;
                break;
            case 1:
                xVel = speed;
                break;
            case 2:
                yVel = -speed;
                break;
            case 3:
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
        if (s instanceof Gremlin || s instanceof Slime) { //|| s instanceof slime
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
        canRemove = true;
    }

    public int getCentreX() {
        return this.xPx + xOffset;
    }

    public int getCentreY() {
        return this.yPx + yOffset;
    }

    public void reset(Game g) {
        stop();
    }
}
