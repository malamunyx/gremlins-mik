package gremlins;

import processing.core.PImage;

import java.util.Iterator;

public class Fireball implements Sprite {
    private Game currentGame;
    private static final int speed = 4;
    int xPx;
    int yPx;
    int xVel;
    int yVel;
    int imgDir;

    public Fireball(int xPx, int yPx, int imgDir) {
        this.xPx = xPx;
        this.yPx = yPx;
        this.imgDir = imgDir;
    }

    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

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
        xPx += xVel;
        yPx += yVel;
    }

    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }
    public int getIndex() {
        return (xPx / 20) + 36 * (yPx / 20);
    }




}
