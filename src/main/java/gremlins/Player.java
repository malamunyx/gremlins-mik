package gremlins;

import processing.core.PImage;

public class Player {
    private final PImage img;
    private int x;
    private int y;

    private int xVel = 0;
    private int yVel = 0;

    /*
     * Determines where the next point of stopping
     */
    private int xMovePt; // will always start from starting pos of X.
    private int yMovePt;
    private boolean xMoving = false; // whether object is moving
    private boolean yMoving = false;


    private static final int speed = 2;

    public Player(PImage img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.xMovePt = x; // movePts start in the original pos.
        this.yMovePt = y;
    }

    public void draw(App app) {
        app.image(img, x, y);

        // check for collisions

        if (!xMoving && (x % 20 == 0)) {
            xVel = 0;
        }
        if (!yMoving && (y % 20 == 0)) {
            yVel = 0;
        }

        x += xVel;
        y += yVel;
    }

    public void left() {
        xMoving = true;
        if ((xMovePt - x)  == 2) // change for 1 pixel
            xMovePt -= 20;
        xVel = -speed;
    }

    public void right() {
        xMoving = true;
        if ((xMovePt - x)  == 2) // change for 1 pixel
            xMovePt += 20;
        xVel = speed;
    }

    public void up() {
        yMoving = true;
        if ((yMovePt - y) == 2) // change for 1 pixel
            yMovePt -= 20;
        yVel = -speed;
    }

    public void down() {
        yMoving = true;
        if ((yMovePt + y)  == 2) // change for 1 pixel
            yMovePt += 20;
        yVel = speed;
    }
    public void yStop() {
        yMoving = false;
    }

    public void xStop() {
        xMoving = false;
    }
}
