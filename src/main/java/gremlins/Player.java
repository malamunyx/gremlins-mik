package gremlins;

// THIS IS WHAT WE USE TO MOVE RELEASE AND STOP AT A GRID
//        if (!xMoving && (x % 20 == 0)) {
//            xVel = 0;
//        }
//        if (!yMoving && (y % 20 == 0)) {
//            yVel = 0;
//        }
////        this.moveHash = getHashIndex();
//        x += xVel;
//        y +=

// CONSIDER USING THE HASHCODE TO DETERMINE.
// HASHCODE: for int x, y,   hashcode = x/20 + width*(y/20)
// x/20 and y/20 are integer divisions (Floor division)

import processing.core.PImage;

public class Player {
    private int x;
    private int y;

    private int xVel;
    private int yVel;

    private boolean xMoving = false;
    private boolean yMoving = false;

    private int tar_x;
    private int tar_y;
    private int xDir = 0;
    private int yDir = 0;



    private static final int speed = 2;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.tar_x = x;
        this.tar_y = y;
        this.xVel = 0;
        this.yVel = 0;
    }

    public int getHashIndex(int x, int y) {
        return (x / 20) + 36 * (y / 20);
    }
    public int getHashIndex() {
        return (this.x / 20) + 36 * (this.y / 20);
    }
    public void draw(PImage img, App app) {
        app.image(img, x, y);

        // can we move? i.e. it's in a square.
        // check for collisions, especially if tile is door etc.
        if (xVel > 0 && app.mapTiles.containsKey(this.getHashIndex() + 1))
            xStop();

        if (xVel < 0 && app.mapTiles.containsKey(this.getHashIndex() - 1))
            xStop();

        if (yVel > 0 && app.mapTiles.containsKey(this.getHashIndex() + 36))
            yStop();

        if (yVel < 0 && app.mapTiles.containsKey(this.getHashIndex() - 36))
            yStop();

        if (xVel != 0)
            yMoving = false;
        if (yVel != 0)
            xMoving = false;

        if (x == tar_x && y == tar_y) {
            xVel = 0;
            yVel = 0;
            if (xMoving) {
                tar_x += xDir * 20;
                xVel = xDir * speed;
            } else if (yMoving) {
                tar_y += yDir * 20;
                yVel = yDir * speed;
            }

        } else { // move towards target
            x += xVel;
            y += yVel;
        }
    }

    public void left(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()-1) ) {
            xMoving = true;
            xDir = -1;
        }
    }

    public void right(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()+1) ) {
            xMoving = true;
            xDir = 1;
        }
    }
    public void up(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()-36) ) {
            yMoving = true;
            yDir = -1;
        }
    }

    public void down(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()+36) ) {
            yMoving = true;
            yDir = 1;
        }
    }

    public void xStop() {
        xMoving = false;
        xDir = 0;
    }

    public void yStop() {
        yMoving = false;
        yDir = 0;
    }
}
