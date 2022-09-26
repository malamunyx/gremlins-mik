package gremlins;

import processing.core.PImage;

import java.util.Iterator;

public class Fireball implements Sprite {
    private final static int xOffset = 10;  // Sprites removed when the MIDDLE hits wall
    private final static int yOffset = 10;
    private final Game currentGame;
    private static final int speed = 4;
    int xPx;
    int yPx;
    int xVel;
    int yVel;
    int imgDir;
    boolean collided;


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

        this.collided = false;
    }

    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (!currentGame.checkWall(getIndex(xPx+xOffset, yPx+yOffset))) {
            xVel = 0;
            yVel = 0;
            setCollided();
        }

        xPx += xVel;
        yPx += yVel;
    }

    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }
    public int getIndex() {
        return ((xPx+xOffset) / 20) + 36 * ((yPx+yOffset) / 20);
    }

    public void setCollided() {
        this.collided = true;
    }

    public boolean collided() {
        return collided;
    }


}
